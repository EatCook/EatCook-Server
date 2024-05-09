package com.itcook.cooking.api.domains.user.service;

import static com.itcook.cooking.infra.email.EmailTemplate.PASSWORD_EMAIL;

import com.itcook.cooking.api.domains.user.dto.request.SignupRequest;
import com.itcook.cooking.api.domains.user.dto.response.AddUserResponse;
import com.itcook.cooking.api.domains.user.dto.response.UserResponse;
import com.itcook.cooking.api.domains.user.service.dto.AddSignupServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.SendEmailServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.VerifyEmailServiceDto;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.AuthCodeRedisService;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import com.itcook.cooking.domain.domains.user.service.UserImageRegisterService;
import com.itcook.cooking.infra.email.EmailSendEvent;
import com.itcook.cooking.infra.s3.ImageUrlDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignupUseCase {

    private final UserDomainService userDomainService;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final UserImageRegisterService userImageRegisterService;
    private final AuthCodeRedisService authCodeRedisService;


    /**
     * 이메일 인증 코드 요청 서비스
     */
    public void sendAuthCodeSignup(SendEmailServiceDto sendEmailServiceDto) {
        userDomainService.checkDuplicateMail(sendEmailServiceDto.email());
        authCodeRedisService.sendAuthCode(sendEmailServiceDto.email());
    }

    /**
     * 계정 찾기 요청 서비스
     */
    public void findUser(SendEmailServiceDto sendEmailServiceDto) {
        userDomainService.findUserByEmail(sendEmailServiceDto.email());
        authCodeRedisService.sendAuthCode(sendEmailServiceDto.email());
    }

    /**
     * 이메일 인증 코드 검증 서비스
     */
    public void verifyAuthCode(VerifyEmailServiceDto verifyEmailServiceDto) {
        authCodeRedisService.verifyAuthCode(verifyEmailServiceDto.email(), verifyEmailServiceDto.authCode());

    }

    // 임시 비밀번호 메일 발송 (이벤트 발생)
    @Transactional
    public void verifyFindUser(VerifyEmailServiceDto verifyEmailServiceDto) {
        authCodeRedisService.verifyAuthCode(verifyEmailServiceDto.email(), verifyEmailServiceDto.authCode());
        String temporaryPassword = userDomainService.issueTemporaryPassword(
            verifyEmailServiceDto.email());
        eventPublisher.publishEvent(
            EmailSendEvent.of(PASSWORD_EMAIL.getSub(), PASSWORD_EMAIL.formatBody(temporaryPassword),
                verifyEmailServiceDto.email()));
    }

    @Transactional
    public UserResponse signup(SignupRequest signupRequest) {
        ItCookUser user = userDomainService.signup(signupRequest.getEmail(),
            passwordEncoder.encode(signupRequest.getPassword()));
        return UserResponse.of(user);
    }

    @Transactional
    public AddUserResponse addSignup(AddSignupServiceDto addSignupRequest) {
        ItCookUser itCookUser = userDomainService.addSignup(addSignupRequest.toEntity(),
            addSignupRequest.toCookingTypes());
        // fileExension이 있을 경우 프로필 이미지 업로드
        ImageUrlDto imageUrlDto = userImageRegisterService.getImageUrlDto(
            addSignupRequest.fileExtension(), itCookUser);

        return AddUserResponse.builder()
            .presignedUrl(imageUrlDto.getUrl())
            .userId(itCookUser.getId())
            .build()
            ;
    }

}
