package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.domains.user.dto.request.SendEmailAuthRequest;
import com.itcook.cooking.api.domains.user.dto.request.SignupRequest;
import com.itcook.cooking.api.domains.user.dto.request.VerifyEmailAuthRequest;
import com.itcook.cooking.api.domains.user.dto.response.AddUserResponse;
import com.itcook.cooking.api.domains.user.dto.response.UserResponse;
import com.itcook.cooking.api.global.annotation.Business;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.common.utils.RandomCodeUtils;
import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import com.itcook.cooking.infra.email.EmailSendEvent;
import com.itcook.cooking.infra.email.EmailTemplate;
import com.itcook.cooking.infra.redis.config.RedisService;
import com.itcook.cooking.infra.s3.ImageUrlDto;
import com.itcook.cooking.infra.s3.S3PresignedUrlService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Slf4j
@Business
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
     *  이메일 인증 코드 검증 서비스
     */
    @Transactional
    public void verifyAuthCode(VerifyEmailAuthRequest verifyEmailAuthRequest) {
        String authCode = (String) redisService.getData(verifyEmailAuthRequest.getEmail());

        if (StringUtils.isEmpty(authCode)) {
            throw new ApiException(UserErrorCode.NO_VERIFY_CODE);
        }
        if (!verifyEmailAuthRequest.getAuthCode().equals(authCode)) {
            throw new ApiException(UserErrorCode.EMAIL_VERIFY_FAIL);
        }
    }


    @Transactional
    public UserResponse signup(SignupRequest signupRequest) {
        signupRequest.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        ItCookUser user = signupRequest.toDomain();
        ItCookUser itCookUser = userDomainService.registerUser(user);
        return UserResponse.of(itCookUser);
    }

    @Transactional
    public AddUserResponse addSignup(ItCookUser user, String fileExtension, List<CookingType> cookingTypes) {
        ImageUrlDto imageUrlDto = ImageUrlDto.builder().build();
        ItCookUser itCookUser = userDomainService.addSignup(user, cookingTypes);
        // fileExension이 있을 경우 프로필 이미지 업로드
        imageUrlDto = getImageUrlDto(fileExtension, imageUrlDto, itCookUser);

        return AddUserResponse.builder()
            .presignedUrl(imageUrlDto.getUrl())
            .userId(itCookUser.getId())
            .build()
            ;
    }

    private ImageUrlDto getImageUrlDto(String fileExtension, ImageUrlDto imageUrlDto,
        ItCookUser itCookUser) {
        if (StringUtils.hasText(fileExtension)) {
            imageUrlDto = s3PresignedUrlService.forUser(itCookUser.getId(),
                fileExtension);
            itCookUser.updateProfile(imageUrlDto.getKey());
        }
        return imageUrlDto;
    }
}
