package com.itcook.cooking.domain.common.events.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserFollowEvent {

    private final String followerNickName;
    private final Long followingId;

    @Builder
    private UserFollowEvent(String followerNickName, Long followingId) {
        this.followerNickName = followerNickName;
        this.followingId = followingId;
    }

    public static UserFollowEvent of(String followerNickName, Long followingId) {
        return UserFollowEvent.builder()
            .followerNickName(followerNickName)
            .followingId(followingId)
            .build()
            ;
    }
}
