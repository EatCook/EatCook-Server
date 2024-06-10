package com.itcook.cooking.domain.common.events.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserFollowedEvent {

    private final String followerNickName;
    private final Long followingId;

    @Builder
    private UserFollowedEvent(String followerNickName, Long followingId) {
        this.followerNickName = followerNickName;
        this.followingId = followingId;
    }

    public static UserFollowedEvent of(String followerNickName, Long followingId) {
        return UserFollowedEvent.builder()
            .followerNickName(followerNickName)
            .followingId(followingId)
            .build()
            ;
    }
}