package com.itcook.cooking.api.domains.user.service;

import static com.itcook.cooking.domain.common.constant.UserConstant.PASSWORD_REGEXP;

import com.itcook.cooking.api.domains.user.dto.request.AddSignupRequest;
import com.itcook.cooking.api.domains.user.dto.request.SendEmailAuthRequest;
import com.itcook.cooking.api.domains.user.dto.request.SignupRequest;
import com.itcook.cooking.api.domains.user.dto.request.VerifyEmailAuthRequest;
import com.itcook.cooking.api.domains.user.dto.response.AddUserResponse;
import com.itcook.cooking.api.domains.user.dto.response.UserResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.common.utils.RandomCodeUtils;
import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import com.itcook.cooking.infra.email.EmailSendEvent;
import com.itcook.cooking.infra.email.EmailTemplate;
import com.itcook.cooking.infra.redis.RedisService;
import com.itcook.cooking.infra.s3.ImageFileExtension;
import com.itcook.cooking.infra.s3.ImageUrlDto;
import com.itcook.cooking.infra.s3.S3PresignedUrlService;
import java.util.List;
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
public class SignupUseCase {

    private final UserDomainService userDomainService;
    private final RedisService redisService;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final S3PresignedUrlService s3PresignedUrlService;


    /**
     * 이메일 인증 코드 요청 서비스
     */
    @Transactional
    public void sendAuthCode(SendEmailAuthRequest sendEmailAuthRequest) {
        userDomainService.findUserByEmail(sendEmailAuthRequest.getEmail());
        String authCode = RandomCodeUtils.generateRandomCode();
        eventPublisher.publishEvent(
            EmailSendEvent.builder()
                .subject(EmailTemplate.AUTH_EMAIL.getSub())
                .body(EmailTemplate.AUTH_EMAIL.formatBody(authCode))
                .to(sendEmailAuthRequest.getEmail())
                .build()
        );
        redisService.setDataWithExpire(sendEmailAuthRequest.getEmail(), authCode, 35L);
    }

    /**
     * 이메일 인증 코드 검증 서비스
     */
    @Transactional
    public void verifyAuthCode(VerifyEmailAuthRequest verifyEmailAuthRequest) {
        String authCode = (String) redisService.getData(verifyEmailAuthRequest.getEmail());
        validateAuthCode(verifyEmailAuthRequest, authCode);
    }

    private void validateAuthCode(VerifyEmailAuthRequest verifyEmailAuthRequest, String authCode) {
        if (!StringUtils.hasText(authCode)) {
            throw new ApiException(UserErrorCode.NO_VERIFY_CODE);
        }
        if (!verifyEmailAuthRequest.getAuthCode().equals(authCode)) {
            throw new ApiException(UserErrorCode.EMAIL_VERIFY_FAIL);
        }
    }


    @Transactional
    public UserResponse signup(SignupRequest signupRequest) {
        Assert.hasText(signupRequest.getPassword(), "패스워드를 입력해야합니다.");
        Assert.isTrue(signupRequest.getPassword().matches(PASSWORD_REGEXP),
            "패스워드는 8자리 이상이어야 하며, 영문과 숫자를 포함해야 합니다.");

        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        ItCookUser user = signupRequest.toDomain();
        ItCookUser savedUser = userDomainService.registerUser(user);
        return UserResponse.of(savedUser);
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
