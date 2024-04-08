package com.itcook.cooking.api.domains.user.service.dto;

import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.repository.dto.PostWithLikedDto;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUserDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPageResponse {

    private Long userId;
    private String nickName;
    private String badge;
    private Long follower;
    private Long following;

    private List<MyPagePostResponse> posts;

    @Builder
    private MyPageResponse(Long userId, String nickName, String badge, Long follower,
        Long following, List<MyPagePostResponse> posts) {
        this.userId = userId;
        this.nickName = nickName;
        this.badge = badge;
        this.follower = follower;
        this.following = following;
        this.posts = posts;
    }

    public static MyPageResponse from(MyPageUserDto myPageUserInfo, List<PostWithLikedDto> posts) {
        return MyPageResponse.builder()
            .userId(myPageUserInfo.getUserId())
            .nickName(myPageUserInfo.getNickName())
            .badge(myPageUserInfo.getBadge())
            .follower(myPageUserInfo.getFollowerCounts())
            .following(myPageUserInfo.getFollowingCounts())
            .posts(posts.stream().map(MyPagePostResponse::from).collect(Collectors.toList()))
            .build()
            ;
    }

}
