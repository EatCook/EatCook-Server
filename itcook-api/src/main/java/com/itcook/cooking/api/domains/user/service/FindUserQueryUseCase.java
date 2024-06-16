package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.domains.user.service.dto.SendEmailServiceDto;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.user.service.AuthCodeRedisService;
import com.itcook.cooking.domain.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FindUserQueryUseCase {

    private final UserService userService;
    private final AuthCodeRedisService authCodeRedisService;

    /**
     * 계정 찾기 요청 서비스
     */
    public void findUser(SendEmailServiceDto sendEmailServiceDto) {
        userService.findUserByEmail(sendEmailServiceDto.email());
        authCodeRedisService.sendAuthCode(sendEmailServiceDto.email());
    }

}
