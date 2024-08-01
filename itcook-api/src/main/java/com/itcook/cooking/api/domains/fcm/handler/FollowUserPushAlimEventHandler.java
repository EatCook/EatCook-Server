package com.itcook.cooking.api.domains.fcm.handler;

import com.itcook.cooking.domain.common.events.user.UserFollowedEvent;
import com.itcook.cooking.domain.domains.infra.fcm.FcmService;
import com.itcook.cooking.domain.domains.infra.fcm.dto.FcmSend;
import com.itcook.cooking.domain.domains.user.domain.adaptor.UserAdaptor;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 팔로우 요청 푸쉬 알림 이벤트 핸들러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FollowUserPushAlimEventHandler {

    private final FcmService fcmService;
    private final UserAdaptor userAdaptor;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void handleUserFollowEvent(UserFollowedEvent userFollowedEvent) {
        log.info("{}의 팔로잉 요청 푸쉬 알림 생성", userFollowedEvent.getFollowerNickName());
        ItCookUser followingUser = userAdaptor.queryUserById(userFollowedEvent.getFollowingId());
        if (!followingUser.isAlim()) {
            return;
        }
        fcmService.sendMessageTo(FcmSend.builder()
            .title("팔로우 요청")
            .body(String.format("%s님이 회원님을 팔로우 했습니다.", userFollowedEvent.getFollowerNickName()))
            .targetUserId(followingUser.getId())
            .build());
    }

}
