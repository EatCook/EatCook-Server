package com.itcook.cooking.api.domains.user.service;

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
public class FindUserCase {

    private final UserService userService;
    private final AuthCodeRedisService authCodeRedisService;

    // 임시 비밀번호 메일 발송 (이벤트 발생)
    public void verifyFindUser(VerifyEmailServiceDto verifyEmailServiceDto) {
        authCodeRedisService.verifyAuthCode(verifyEmailServiceDto.email(), verifyEmailServiceDto.authCode());
    }

    public void findUserChangeNewPassword(FindUserChangePasswordServiceDto serviceDto) {
        userService.changePassword(serviceDto.toEntity());
    }

}
