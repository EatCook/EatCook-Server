package com.itcook.cooking.domain.domains.user.service.handler;

import com.itcook.cooking.domain.domains.infra.redis.RedisService;
import com.itcook.cooking.domain.common.events.user.UserLeavedEvent;
import com.itcook.cooking.domain.domains.post.domain.adaptor.PostAdaptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 회원 탈퇴 이벤트 핸들러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class UserLeaveEventHandler {

    private final RedisService redisService;
    private final PostAdaptor postAdaptor;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void deleteToken(UserLeavedEvent userLeavedEvent) {
        String email = userLeavedEvent.getEmail();
        log.info("유저 탈퇴 이벤트 발생, 유저 이메일: {}", email);
        redisService.deleteKeysContaining(email);
        postAdaptor.updatePostsDisabledBy(userLeavedEvent.getUserId());
    }

}
