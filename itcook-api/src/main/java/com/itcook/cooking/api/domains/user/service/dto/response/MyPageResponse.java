package com.itcook.cooking.api.domains.user.service.dto.response;

import com.itcook.cooking.api.global.dto.PageResponse;
import com.itcook.cooking.domain.domains.post.repository.dto.PostWithLikedDto;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUserDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPageResponse {

    private Long userId;
    private String nickName;
    private String badge;
    private Long follower;
    private Long following;
    private ProviderType providerType;

    //    private List<MyPagePostResponse> posts;
    private PageResponse<PostWithLikedDto> posts;

    @Builder
    private MyPageResponse(Long userId, String nickName, String badge, Long follower,
        Long following, ProviderType providerType, PageResponse<PostWithLikedDto> posts) {
        this.userId = userId;
        this.nickName = nickName;
        this.badge = badge;
        this.follower = follower;
        this.following = following;
        this.providerType = providerType;
        this.posts = posts;
    }

    public static MyPageResponse of(MyPageUserDto myPageUserInfo,
        PageResponse<PostWithLikedDto> posts) {
        return MyPageResponse.builder()
            .userId(myPageUserInfo.getUserId())
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
