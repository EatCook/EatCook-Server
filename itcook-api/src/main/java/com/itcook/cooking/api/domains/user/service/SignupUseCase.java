package com.itcook.cooking.api.domains.user.service;

import static com.itcook.cooking.domain.common.constant.UserConstant.PASSWORD_REGEXP;

import com.itcook.cooking.api.domains.user.dto.request.AddSignupRequest;
import com.itcook.cooking.api.domains.user.dto.request.SignupRequest;
import com.itcook.cooking.api.domains.user.dto.response.AddUserResponse;
import com.itcook.cooking.api.domains.user.dto.response.UserResponse;
import com.itcook.cooking.api.domains.user.service.dto.SendEmailServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.VerifyEmailServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.response.VerifyFindUserResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.common.utils.RandomCodeUtils;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.validator.UserValidator;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import com.itcook.cooking.infra.email.EmailSendEvent;
import com.itcook.cooking.infra.email.EmailTemplate;
import com.itcook.cooking.infra.redis.RedisService;
import com.itcook.cooking.infra.s3.ImageFileExtension;
import com.itcook.cooking.infra.s3.ImageUrlDto;
import com.itcook.cooking.infra.s3.S3PresignedUrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Slf4j
@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignupUseCase {

    private final UserDomainService userDomainService;
    private final RedisService redisService;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final S3PresignedUrlService s3PresignedUrlService;


    /**
     * 이메일 인증 코드 요청 서비스
     */
    public void sendAuthCodeSignup(SendEmailServiceDto sendEmailServiceDto) {
        userDomainService.findUserByEmail(sendEmailServiceDto.email());
        sendAuthCode(sendEmailServiceDto);
    }

    /**
     * 계정 찾기 요청 서비스
     */
    public void findUser(SendEmailServiceDto sendEmailServiceDto) {
        userDomainService.fetchFindByEmail(sendEmailServiceDto.email());
        sendAuthCode(sendEmailServiceDto);
    }

    private void sendAuthCode(SendEmailServiceDto sendEmailServiceDto) {
        String authCode = RandomCodeUtils.generateRandomCode();
        eventPublisher.publishEvent(
            EmailSendEvent.builder()
                .subject(EmailTemplate.AUTH_EMAIL.getSub())
                .body(EmailTemplate.AUTH_EMAIL.formatBody(authCode))
                .to(sendEmailServiceDto.email())
                .build()
        );
        redisService.setDataWithExpire(sendEmailServiceDto.email(), authCode, 180L);
    }

    /**
     * 이메일 인증 코드 검증 서비스
     */
    public void verifyAuthCode(VerifyEmailServiceDto verifyEmailServiceDto) {
        String authCode = (String) redisService.getData(verifyEmailServiceDto.email());
        validateAuthCode(verifyEmailServiceDto, authCode);
    }

    private void validateAuthCode(VerifyEmailServiceDto verifyEmailServiceDto, String authCode) {
        if (!StringUtils.hasText(authCode)) {
            throw new ApiException(UserErrorCode.NO_VERIFY_CODE);
        }
        if (!verifyEmailServiceDto.authCode().equals(authCode)) {
            throw new ApiException(UserErrorCode.EMAIL_VERIFY_FAIL);
        }
    }

    @Transactional
    public VerifyFindUserResponse verifyFindUser(VerifyEmailServiceDto verifyEmailServiceDto) {
        verifyAuthCode(verifyEmailServiceDto);
        String temporaryPassword = createTemporaryPassword(verifyEmailServiceDto);
        return VerifyFindUserResponse.of(verifyEmailServiceDto.email(), temporaryPassword);
    }

    private String createTemporaryPassword(VerifyEmailServiceDto verifyEmailServiceDto) {
        ItCookUser itCookUser = userDomainService.fetchFindByEmail(verifyEmailServiceDto.email());
        String temporaryPassword = RandomCodeUtils.generateTemporaryPassword();
        itCookUser.changePassword(passwordEncoder.encode(temporaryPassword));
        return temporaryPassword;
    }


    @Transactional
    public UserResponse signup(SignupRequest signupRequest) {
        ItCookUser user = userDomainService.signup(signupRequest.getEmail(),
            passwordEncoder.encode(signupRequest.getPassword()));
        return UserResponse.of(user);
    }

    @Transactional
    public AddUserResponse addSignup(AddSignupRequest addSignupRequest) {
        ImageUrlDto imageUrlDto = ImageUrlDto.builder().build();
        ItCookUser itCookUser = userDomainService.addSignup(addSignupRequest.toEntity(),
            addSignupRequest.toCookingTypes());
        // fileExension이 있을 경우 프로필 이미지 업로드
        imageUrlDto = getImageUrlDto(addSignupRequest.getFileExtension(), imageUrlDto, itCookUser);

        return AddUserResponse.builder()
            .presignedUrl(imageUrlDto.getUrl())
            .userId(itCookUser.getId())
            .build()
            ;
    }

    private ImageUrlDto getImageUrlDto(String fileExtension, ImageUrlDto imageUrlDto,
        ItCookUser itCookUser) {
        if (StringUtils.hasText(fileExtension)) {
            ImageFileExtension imageFileExtension = ImageFileExtension.fromFileExtension(
                fileExtension);
            imageUrlDto = s3PresignedUrlService.forUser(itCookUser.getId(),
                imageFileExtension.getUploadExtension());
            itCookUser.updateProfile(imageUrlDto.getKey());
        }
        return imageUrlDto;
    }
}
