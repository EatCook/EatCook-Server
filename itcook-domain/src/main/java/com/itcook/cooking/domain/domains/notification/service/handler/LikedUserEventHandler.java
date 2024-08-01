package com.itcook.cooking.domain.domains.notification.service.handler;

import com.itcook.cooking.domain.common.events.user.UserLikedEvent;
import com.itcook.cooking.domain.domains.notification.domain.adapter.NotificationAdapter;
import com.itcook.cooking.domain.domains.notification.domain.entity.Notification;
import com.itcook.cooking.domain.domains.notification.domain.entity.NotificationType;
import com.itcook.cooking.domain.domains.post.domain.adaptor.PostAdaptor;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
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
 *  좋아요 요청 알림 이벤트 핸들러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LikedUserEventHandler {

    private final NotificationAdapter notificationAdapter;
    private final PostAdaptor postAdaptor;
    private final UserAdaptor userAdaptor;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void handleLikedUserEvent(UserLikedEvent userLikedEvent) {
        Post post = postAdaptor.findByIdOrElseThrow(userLikedEvent.getPostId());
        ItCookUser fromUser = userAdaptor.queryUserById(userLikedEvent.getFromUserId());// 좋아요 누른 사람
        ItCookUser postWriter = userAdaptor.queryUserById(post.getUserId());
        log.info("좋아요 요청 알림 : {}님이 {}의 {}번의 게시글을 좋아합니다", fromUser.getNickName(),
            postWriter.getNickName(), userLikedEvent.getPostId());
        Notification likeAlarm = Notification.createLikeAlarm(
            fromUser.getNickName(),
            NotificationType.LIKE,
            postWriter.getId(),
            userLikedEvent.getPostId()
        );
        notificationAdapter.save(likeAlarm);
    }
}
