package com.itcook.cooking.domain.domains.user.service.dto.response;

import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class MyPageUserInfoResponse {

    private Long userId;
    private String email;
    private String userImagePath;
    private String nickName;
    private String badge;
    private Long followerCounts;
    private Long followingCounts;
    private ProviderType providerType;

    @Builder
    private MyPageUserInfoResponse(Long userId, String email,String userImagePath,String nickName, String badge, Long followerCounts,
        Long followingCounts, ProviderType providerType) {
        this.userId = userId;
        this.email = email;
        this.nickName = nickName;
        this.badge = badge;
        this.followerCounts = followerCounts;
        this.followingCounts = followingCounts;
        this.providerType = providerType;
        this.userImagePath = userImagePath;
    }

    public static MyPageUserInfoResponse of(ItCookUser itCookUser, Long followerCounts) {
        return MyPageUserInfoResponse.builder()
            .userId(itCookUser.getId())
            .email(itCookUser.getEmail())
            .userImagePath(itCookUser.getProfile())
            .nickName(itCookUser.getNickName())
            .badge(itCookUser.getBadgeName())
            .providerType(itCookUser.getProviderType())
            .followerCounts(followerCounts)
            .followingCounts(itCookUser.getFollowingCounts())
            .build();

    }
}
