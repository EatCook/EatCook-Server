package com.itcook.cooking.domain.domains.user.service;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserDomainService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public void findUserByEmail(String email) {
        userRepository.findByEmail(email)
            .ifPresent(it -> {
                throw new ApiException(UserErrorCode.ALREADY_EXISTS_USER);
            });
    }


//    @Transactional
//    public void sendAuthCode(EmailRequest emailRequest) {
////        log.info("{}", TransactionSynchronizationManager.getCurrentTransactionName());
////        log.info("{}", TransactionSynchronizationManager.isSynchronizationActive());
//        userRepository.findByEmail(emailRequest.getEmail())
//            .ifPresent(it -> {throw new ApiException(UserErrorCode.ALREADY_EXISTS_USER);});
//        String authCode = RandomCodeUtils.generateRandomCode();
//        eventPublisher.publishEvent(
//            EmailSendEvent.builder()
//                .subject(EmailTemplate.AUTH_EMAIL.getSub())
//                .body(EmailTemplate.AUTH_EMAIL.formatBody(authCode))
//                .to(emailRequest.getEmail())
//                .build()
//        );
//        redisService.setDataWithExpire(emailRequest.getEmail(), authCode, 30L);
//    }
}
