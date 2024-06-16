package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.domains.user.dto.request.SignupRequest;
import com.itcook.cooking.api.domains.user.dto.response.UserResponse;
import com.itcook.cooking.api.domains.user.service.dto.AddSignupServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.SendEmailServiceDto;
import com.itcook.cooking.domain.common.annotation.UseCase;
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
public class SignupQueryUseCase {

    private final UserService userService;
    private final AuthCodeRedisService authCodeRedisService;


    /**
     * 이메일 인증 코드 요청 서비스
     */
    public void sendAuthCodeSignup(SendEmailServiceDto sendEmailServiceDto) {
        userService.checkDuplicateMail(sendEmailServiceDto.email());
        authCodeRedisService.sendAuthCode(sendEmailServiceDto.email());
    }

}
