package com.itcook.cooking.domain.domains.post.domain.repository.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyRecipeResponse {

    private Long postId;
    private String postImagePath;
    private String recipeName;
    private String introduction;
    private Long likeCounts;

    @Builder
    public MyRecipeResponse(Long postId, String postImagePath, String recipeName,
        String introduction,
        Long likeCounts) {
        this.postId = postId;
        this.postImagePath = postImagePath;
        this.recipeName = recipeName;
        this.introduction = introduction;
        this.likeCounts = likeCounts;
    }
}
