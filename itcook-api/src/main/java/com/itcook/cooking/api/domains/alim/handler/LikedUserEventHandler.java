package com.itcook.cooking.api.domains.alim.handler;

import com.itcook.cooking.domain.common.events.user.UserLikedEvent;
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
 *  좋아요 요청 알림 이벤트 핸들러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LikedUserEventHandler {

    private final NotificationAdapter notificationAdapter;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void handlerLikedUserEvent(UserLikedEvent userLikedEvent) {
        log.info("{}의 좋아요 요청 알림 생성", userLikedEvent.getFromUserNickName());
        Notification likeAlarm = Notification.createLikeAlarm(
            userLikedEvent.getFromUserNickName(),
            NotificationType.LIKE,
            userLikedEvent.getToUserId(),
            userLikedEvent.getPostId()
        );
        notificationAdapter.save(likeAlarm);
    }
}
