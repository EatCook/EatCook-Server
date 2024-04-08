package com.itcook.cooking.domain.domains.post.repository.dto;

import com.itcook.cooking.domain.domains.post.entity.Post;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostWithLikedDto {

    private Long postId;
    private String postImagePath;
    private String recipeName;
    private String introduction;
    private Long likeCounts;

    @Builder
    public PostWithLikedDto(Long postId, String postImagePath, String recipeName,
        String introduction,
        Long likeCounts) {
        this.postId = postId;
        this.postImagePath = postImagePath;
        this.recipeName = recipeName;
        this.introduction = introduction;
        this.likeCounts = likeCounts;
    }
//
//    public PostWithLikedDto from(PostWithLikedDto post) {
//        return PostWithLikedDto.builder()
//            .postId(post.getPostId())
//            .postImagePath(post.getPostImagePath())
//            .recipeName(post.getRecipeName())
//            .introduction(post.getIntroduction())
//            .likeCounts(post.getLikeCounts())
//            .build();
//    }
}
