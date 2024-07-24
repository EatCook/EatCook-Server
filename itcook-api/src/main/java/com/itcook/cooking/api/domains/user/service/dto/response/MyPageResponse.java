package com.itcook.cooking.api.domains.user.service.dto.response;

import com.itcook.cooking.api.global.dto.PageResponse;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.response.MyRecipeResponse;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageUserInfoResponse;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPageResponse {

    private Long userId;
    private String email;
    private String nickName;
    private String badge;
    private Long follower;
    private Long following;
    private ProviderType providerType;

    //    private List<MyPagePostResponse> posts;
    private PageResponse<MyRecipeResponse> posts;

    @Builder
    private MyPageResponse(Long userId, String email,String nickName, String badge, Long follower,
        Long following, ProviderType providerType, PageResponse<MyRecipeResponse> posts) {
        this.userId = userId;
        this.email = email;
        this.nickName = nickName;
        this.badge = badge;
        this.follower = follower;
        this.following = following;
        this.providerType = providerType;
        this.posts = posts;
    }

    public static MyPageResponse of(MyPageUserInfoResponse myPageUserInfo,
        PageResponse<MyRecipeResponse> posts) {
        return MyPageResponse.builder()
            .userId(myPageUserInfo.getUserId())
            .email(myPageUserInfo.getEmail())
            .nickName(myPageUserInfo.getNickName())
            .badge(myPageUserInfo.getBadge())
            .follower(myPageUserInfo.getFollowerCounts())
            .following(myPageUserInfo.getFollowingCounts())
            .providerType(myPageUserInfo.getProviderType())
            .posts(posts)
            .build()
            ;
    }

}
