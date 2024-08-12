package com.itcook.cooking.api.domains.user.service;

import static com.itcook.cooking.domain.common.constant.UserConstant.EMAIL_PREFIX;

import com.itcook.cooking.api.domains.user.service.dto.FindUserChangePasswordServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.VerifyEmailServiceDto;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.user.service.AuthCodeRedisService;
import com.itcook.cooking.domain.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class FindUserUseCase {

    private final UserService userService;
    private final AuthCodeRedisService authCodeRedisService;

    public void verifyFindUser(VerifyEmailServiceDto verifyEmailServiceDto) {
        authCodeRedisService.verifyAuthCode(EMAIL_PREFIX + verifyEmailServiceDto.email(), verifyEmailServiceDto.authCode());
    }

    public void findUserChangeNewPassword(FindUserChangePasswordServiceDto serviceDto) {
        userService.changePassword(serviceDto.toEntity());
    }

}
