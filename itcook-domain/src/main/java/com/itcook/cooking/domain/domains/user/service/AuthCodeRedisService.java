package com.itcook.cooking.domain.domains.user.service;

import static com.itcook.cooking.domain.infra.email.EmailTemplate.AUTH_EMAIL;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.common.utils.RandomCodeUtils;
import com.itcook.cooking.domain.common.events.email.EmailSendEvent;
import com.itcook.cooking.domain.infra.redis.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * 회원 가입 도메인 서비스
 */
@Service
@RequiredArgsConstructor
public class AuthCodeRedisService {

    private final RedisService redisService;
    private final ApplicationEventPublisher eventPublisher;

    public void verifyAuthCode(String email, String submitCode) {
        String authCode = (String) redisService.getData(email);
        validateAuthCode(submitCode, authCode);
    }

    private void validateAuthCode(String submitCode, String authCode) {
        if (!StringUtils.hasText(authCode)) {
            throw new ApiException(UserErrorCode.NO_VERIFY_CODE);
        }
        if (!submitCode.equals(authCode)) {
            throw new ApiException(UserErrorCode.EMAIL_VERIFY_FAIL);
        }
    }

    public void sendAuthCode(String toEmail) {
        String authCode = RandomCodeUtils.generateRandomCode();
        redisService.setDataWithExpire(toEmail, authCode, 180L);
        eventPublisher.publishEvent(
            EmailSendEvent.of(AUTH_EMAIL.getSub(), AUTH_EMAIL.formatBody(authCode),
                toEmail));
    }



}
