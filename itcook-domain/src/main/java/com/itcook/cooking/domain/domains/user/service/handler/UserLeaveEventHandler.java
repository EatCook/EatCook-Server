package com.itcook.cooking.domain.domains.user.service.handler;

import com.itcook.cooking.domain.infra.redis.RedisService;
import com.itcook.cooking.domain.common.events.user.UserLeaveEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 회원 탈퇴 이벤트 핸들러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserLeaveEventHandler {

    private final RedisService redisService;

    @Async
    @TransactionalEventListener
    public void deleteToken(UserLeaveEvent userLeaveEvent) {
        String email = userLeaveEvent.email();
        log.info("유저 탈퇴 이벤트 발생, 유저 이메일: {}", email);
        redisService.deleteData(email);
    }

}
