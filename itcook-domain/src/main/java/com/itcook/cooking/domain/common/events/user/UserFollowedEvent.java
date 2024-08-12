package com.itcook.cooking.domain.common.events.user;

import com.itcook.cooking.domain.common.events.DomainEvent;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class UserFollowedEvent extends DomainEvent {

    private final Long followerId;
    private final String followerNickName;
    private final Long followingId;

    @Builder
    private UserFollowedEvent(Long followerId, String followerNickName, Long followingId) {
        this.followerId = followerId;
        this.followerNickName = followerNickName;
        this.followingId = followingId;
    }

    public static UserFollowedEvent of(Long followerId,String followerNickName, Long followingId) {
        return UserFollowedEvent.builder()
            .followerId(followerId)
            .followerNickName(followerNickName)
            .followingId(followingId)
            .build()
            ;
    }
}
