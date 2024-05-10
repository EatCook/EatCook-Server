package com.itcook.cooking.domain.common.events.user;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserFollowEvent {

    private String followerNickName;
    private Long followingId;

    @Builder
    public UserFollowEvent(String followerNickName, Long followingId) {
        this.followerNickName = followerNickName;
        this.followingId = followingId;
    }
}
