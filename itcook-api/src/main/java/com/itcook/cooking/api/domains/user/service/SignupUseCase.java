package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.domains.user.dto.request.SignupRequest;
import com.itcook.cooking.api.domains.user.dto.response.UserResponse;
import com.itcook.cooking.api.domains.user.service.dto.AddSignupServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.CheckNickNameServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.SignupServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.VerifyEmailServiceDto;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.entity.dto.AddSignupDomainResponse;
import com.itcook.cooking.domain.domains.user.service.AuthCodeRedisService;
import com.itcook.cooking.domain.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class SignupUseCase {

    private final UserService userService;
    private final AuthCodeRedisService authCodeRedisService;


    /**
     * 이메일 인증 코드 검증 서비스
     */
    public void verifyAuthCode(VerifyEmailServiceDto verifyEmailServiceDto) {
        authCodeRedisService.verifyAuthCode(verifyEmailServiceDto.email(), verifyEmailServiceDto.authCode());

    }

    public UserResponse signup(SignupServiceDto signupServiceDto) {
        ItCookUser user = userService.signup(signupServiceDto.toEntity());
        return UserResponse.of(user);
    }

    public AddSignupDomainResponse addSignup(AddSignupServiceDto addSignupRequest) {
        return userService.addSignup(
            addSignupRequest.toEntity()
            , addSignupRequest.fileExtension()
            , addSignupRequest.toCookingTypes());
    }

    public void checkNickName(CheckNickNameServiceDto checkNickNameServiceDto) {
        userService.checkDuplicateNickName(checkNickNameServiceDto.toEntity());
    }
}
