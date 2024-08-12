package com.itcook.cooking.domain.common.events.user;

import com.itcook.cooking.domain.common.events.DomainEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserLikedEvent extends DomainEvent {

    private final Long postId;
    private final Long fromUserId; // 좋아요 요청 게시물의 작성자

    @Builder
    private UserLikedEvent(Long postId, Long fromUserId) {
        this.postId = postId;
        this.fromUserId = fromUserId;
    }

    public static UserLikedEvent of(Long postId, Long fromUserId) {
        return UserLikedEvent.builder()
            .postId(postId)
            .fromUserId(fromUserId)
            .build();
    }
}
