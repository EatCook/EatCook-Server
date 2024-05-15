package com.itcook.cooking.infra.redis.event;

import com.itcook.cooking.infra.redis.RedisService;
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
public class UserLeaveEventListener {

    private final RedisService redisService;

    @Async
    @TransactionalEventListener
    public void deleteToken(UserLeaveEvent userLeaveEvent) {
        String email = userLeaveEvent.email();
        log.info("유저 탈퇴 이벤트 발생, 유저 이메일: {}", email);
        redisService.deleteData(email);
    }

}
