package com.itcook.cooking.domain.common.events.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserLikedEvent {

    private final String fromUserNickName; // 좋아요 누른 유저의 닉네임
    private final Long postId;
    private final Long toUserId; // 좋아요 요청 게시물의 작성자

    @Builder
    private UserLikedEvent(String fromUserNickName, Long postId, Long toUserId) {
        this.fromUserNickName = fromUserNickName;
        this.postId = postId;
        this.toUserId = toUserId;
    }

    public static UserLikedEvent of(String fromUserNickName, Long postId, Long toUserId) {
        return UserLikedEvent.builder()
            .fromUserNickName(fromUserNickName)
            .postId(postId)
            .toUserId(toUserId)
            .build();
    }
}
