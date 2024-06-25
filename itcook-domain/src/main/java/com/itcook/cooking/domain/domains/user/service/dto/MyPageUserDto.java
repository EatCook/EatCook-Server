package com.itcook.cooking.domain.domains.user.service.dto;

import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MyPageUserDto {

    private Long userId;
    private String nickName;
    private String badge;
    private Long followerCounts;
    private Long followingCounts;
    private ProviderType providerType;

    @Builder
    private MyPageUserDto(Long userId, String nickName, String badge, Long followerCounts,
        Long followingCounts, ProviderType providerType) {
        this.userId = userId;
        this.nickName = nickName;
        this.badge = badge;
        this.followerCounts = followerCounts;
        this.followingCounts = followingCounts;
        this.providerType = providerType;
    }

    public static MyPageUserDto of(ItCookUser itCookUser, Long followerCounts) {
        return MyPageUserDto.builder()
            .userId(itCookUser.getId())
            .nickName(itCookUser.getNickName())
            .badge(itCookUser.getBadgeName())
            .providerType(itCookUser.getProviderType())
            .followerCounts(followerCounts)
            .followingCounts(itCookUser.getFollowingCounts())
            .build();

    }
}
