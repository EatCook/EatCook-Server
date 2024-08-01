package com.itcook.cooking.api.domains.fcm.handler;

import com.itcook.cooking.domain.common.events.user.UserLikedEvent;
import com.itcook.cooking.domain.domains.infra.fcm.FcmService;
import com.itcook.cooking.domain.domains.infra.fcm.dto.FcmSend;
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
 * 좋아요 요청 푸쉬 알림 이벤트 핸들러
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LikeUserPushAlimEventHandler {

    private final FcmService fcmService;
    private final PostAdaptor postAdaptor;
    private final UserAdaptor userAdaptor;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void handleUserLikedEvent(UserLikedEvent event) {
        Post post = postAdaptor.findByIdOrElseThrow(event.getPostId());
        ItCookUser fromUser = userAdaptor.queryUserById(event.getFromUserId());// 좋아요 누른 사람
        ItCookUser postWriter = userAdaptor.queryUserById(post.getUserId());
        log.info("좋아요 푸쉬 요청 알림 : {}님이 {}의 {}번의 게시글을 좋아합니다", fromUser.getNickName(),
            postWriter.getNickName(), event.getPostId());
        if (!postWriter.isAlim()) {
            return;
        }
        fcmService.sendMessageTo(FcmSend.builder()
            .title("좋아요 요청")
            .body(String.format("%s님이 회원님의 레시피를 좋아합니다.", fromUser.getNickName()))
            .targetUserId(postWriter.getId())
            .build());
    }
}
