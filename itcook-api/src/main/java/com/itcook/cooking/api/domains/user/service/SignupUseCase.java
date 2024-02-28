package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.domains.user.dto.request.SendEmailAuthRequest;
import com.itcook.cooking.api.domains.user.dto.request.VerifyEmailAuthRequest;
import com.itcook.cooking.api.global.annotation.Business;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.common.utils.RandomCodeUtils;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import com.itcook.cooking.infra.email.EmailSendEvent;
import com.itcook.cooking.infra.email.EmailTemplate;
import com.itcook.cooking.infra.redis.config.RedisService;
import io.jsonwebtoken.lang.Assert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

@Slf4j
@Business
@RequiredArgsConstructor
public class SignupUseCase {

    private final UserDomainService userDomainService;
    private final RedisService redisService;
    private final ApplicationEventPublisher eventPublisher;


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
        redisService.setDataWithExpire(sendEmailAuthRequest.getEmail(), authCode, 30L);
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



}
