package com.itcook.cooking.api.domains.alim.handler;

import com.itcook.cooking.domain.common.events.user.UserFollowEvent;
import com.itcook.cooking.domain.domains.notification.adapter.NotificationAdapter;
import com.itcook.cooking.domain.domains.notification.entity.Notification;
import com.itcook.cooking.domain.domains.notification.entity.NotificationType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * 팔로우 요청시 발생하는 이벤트
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class FollowUserEventHandler {

    private final NotificationAdapter notificationAdapter;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void handlerFollowUserEvent(UserFollowEvent userFollowEvent) {
        log.info("{}의 팔로잉 요청 알림 생성", userFollowEvent.getFollowerNickName());
        Notification alarm = Notification.createFollowAlarm(userFollowEvent.getFollowerNickName(),
            NotificationType.FOLLOW,
            userFollowEvent.getFollowingId());
        notificationAdapter.save(alarm);
    }


}
