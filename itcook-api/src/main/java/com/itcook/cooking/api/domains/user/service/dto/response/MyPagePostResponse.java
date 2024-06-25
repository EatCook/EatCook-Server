package com.itcook.cooking.api.domains.user.service.dto.response;

import com.itcook.cooking.domain.domains.post.domain.repository.dto.PostWithLikedDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPagePostResponse {

    private String imageFilePath;
    private String title;
    private String introduction;
    private Long liked;

    @Builder
    private MyPagePostResponse(String imageFilePath, String title, String introduction,
        Long liked) {
        this.imageFilePath = imageFilePath;
        this.title = title;
        this.introduction = introduction;
        this.liked = liked;
    }

    public static MyPagePostResponse from(PostWithLikedDto post) {
        return MyPagePostResponse.builder()
            .imageFilePath(post.getPostImagePath())
            .title(post.getRecipeName())
            .introduction(post.getIntroduction())
            .liked(post.getLikeCounts())
            .build()
            ;
    }
}
