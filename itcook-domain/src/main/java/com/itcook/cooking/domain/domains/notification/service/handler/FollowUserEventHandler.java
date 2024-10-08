package com.itcook.cooking.domain.domains.notification.service.handler;

import com.itcook.cooking.domain.common.events.user.UserFollowedEvent;
import com.itcook.cooking.domain.domains.notification.domain.adapter.NotificationAdapter;
import com.itcook.cooking.domain.domains.notification.domain.entity.Notification;
import com.itcook.cooking.domain.domains.notification.domain.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 팔로우 요청시 발생하는 이벤트 핸들
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FollowUserEventHandler {

    private final NotificationAdapter notificationAdapter;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void handlerFollowUserEvent(UserFollowedEvent userFollowedEvent) {
        log.info("{}의 팔로잉 요청 알림 생성", userFollowedEvent.getFollowerNickName());
        Notification alarm = Notification.createFollowAlarm(userFollowedEvent.getFollowerNickName(),
            NotificationType.FOLLOW,
            userFollowedEvent.getFollowingId());
        notificationAdapter.save(alarm);
    }


}
