package com.itcook.cooking.domain.domains.user.service;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.common.utils.RandomCodeUtils;
import com.itcook.cooking.domain.domains.user.dto.request.EmailRequest;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import com.itcook.cooking.infra.email.AuthCodeService;
import com.itcook.cooking.infra.email.EmailSendEvent;
import com.itcook.cooking.infra.email.EmailTemplate;
import com.itcook.cooking.infra.redis.config.RedisService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final RedisService redisService;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    /**
     * 이메일 인증 코드 요청 서비스
     */
    @Transactional
    public void sendAuthCode(EmailRequest emailRequest) {
        log.info("{}", TransactionSynchronizationManager.getCurrentTransactionName());
        log.info("{}", TransactionSynchronizationManager.isSynchronizationActive());
        Optional<ItCookUser> findEmail = userRepository.findByEmail(emailRequest.getEmail());
        if (findEmail.isPresent()) {
            throw new ApiException(UserErrorCode.ALREADY_EXISTS_USER);
        }
        String authCode = RandomCodeUtils.generateRandomCode();
        eventPublisher.publishEvent(
            EmailSendEvent.builder()
                .subject(EmailTemplate.AUTH_EMAIL.getSub())
                .body(EmailTemplate.AUTH_EMAIL.formatBody(authCode))
                .to(emailRequest.getEmail())
                .build()
        );
        redisService.setDataWithExpire(emailRequest.getEmail(), authCode, 30L);
    }
}
