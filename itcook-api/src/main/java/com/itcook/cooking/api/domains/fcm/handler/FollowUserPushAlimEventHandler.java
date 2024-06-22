package com.itcook.cooking.api.domains.fcm.handler;

import com.itcook.cooking.domain.common.events.user.UserFollowedEvent;
import com.itcook.cooking.domain.domains.infra.fcm.FcmService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowUserPushAlimEventHandler {

    private final FcmService fcmService;

    @Async
    @TransactionalEventListener
    public void handleUserFollowEvent(UserFollowedEvent userFollowedEvent) {
        log.info("팔로우 요청 FCM 알림");
//        fcmService.sendMessageTo();
    }

}
