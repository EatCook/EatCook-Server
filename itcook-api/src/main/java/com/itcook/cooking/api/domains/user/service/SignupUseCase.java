package com.itcook.cooking.api.domains.user.service;

import static com.itcook.cooking.domain.infra.email.EmailTemplate.PASSWORD_EMAIL;

import com.itcook.cooking.api.domains.user.dto.request.SignupRequest;
import com.itcook.cooking.api.domains.user.dto.response.UserResponse;
import com.itcook.cooking.api.domains.user.service.dto.AddSignupServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.SendEmailServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.VerifyEmailServiceDto;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.common.events.email.EmailSendEvent;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.dto.AddSignupDomainResponse;
import com.itcook.cooking.domain.domains.user.service.AuthCodeRedisService;
import com.itcook.cooking.domain.domains.user.service.UserService;
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

    private final UserService userService;
    private final ApplicationEventPublisher eventPublisher;
    private final PasswordEncoder passwordEncoder;
    private final AuthCodeRedisService authCodeRedisService;


    /**
     * 이메일 인증 코드 요청 서비스
     */
    public void sendAuthCodeSignup(SendEmailServiceDto sendEmailServiceDto) {
        userService.checkDuplicateMail(sendEmailServiceDto.email());
        authCodeRedisService.sendAuthCode(sendEmailServiceDto.email());
    }

    /**
     * 계정 찾기 요청 서비스
     */
    public void findUser(SendEmailServiceDto sendEmailServiceDto) {
        userService.findUserByEmail(sendEmailServiceDto.email());
        authCodeRedisService.sendAuthCode(sendEmailServiceDto.email());
    }

    /**
     * 이메일 인증 코드 검증 서비스
     */
    public void verifyAuthCode(VerifyEmailServiceDto verifyEmailServiceDto) {
        authCodeRedisService.verifyAuthCode(verifyEmailServiceDto.email(), verifyEmailServiceDto.authCode());

    }

    // 임시 비밀번호 메일 발송 (이벤트 발생)
    public void verifyFindUser(VerifyEmailServiceDto verifyEmailServiceDto) {
        authCodeRedisService.verifyAuthCode(verifyEmailServiceDto.email(), verifyEmailServiceDto.authCode());
        String temporaryPassword = userService.issueTemporaryPassword(
            verifyEmailServiceDto.email());
        eventPublisher.publishEvent(
            EmailSendEvent.of(PASSWORD_EMAIL.getSub(), PASSWORD_EMAIL.formatBody(temporaryPassword),
                verifyEmailServiceDto.email()));
    }

    @Transactional
    public UserResponse signup(SignupRequest signupRequest) {
        ItCookUser user = userService.signup(signupRequest.getEmail(),
            passwordEncoder.encode(signupRequest.getPassword()));
        return UserResponse.of(user);
    }

    @Transactional
    public AddSignupDomainResponse addSignup(AddSignupServiceDto addSignupRequest) {
        return userService.addSignup(
            addSignupRequest.toEntity()
            , addSignupRequest.fileExtension()
            , addSignupRequest.toCookingTypes());
    }

}
