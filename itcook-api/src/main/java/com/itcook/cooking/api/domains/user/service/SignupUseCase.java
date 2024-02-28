package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.domains.user.dto.request.SendEmailAuthRequest;
import com.itcook.cooking.api.global.annotation.Business;
import com.itcook.cooking.domain.common.utils.RandomCodeUtils;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import com.itcook.cooking.infra.email.EmailSendEvent;
import com.itcook.cooking.infra.email.EmailTemplate;
import com.itcook.cooking.infra.redis.config.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(readOnly = true)
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




}
