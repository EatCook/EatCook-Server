package com.itcook.cooking.domain.domains.post.domain.repository.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HomeSpecialDto {
    private Long postId;
    private String postImagePath;
    private String recipeName;
    private String introduction;
    private Integer recipeTime;
    private Long likedCounts;
    private Boolean likedCheck;
    private Boolean archiveCheck;

    public HomeSpecialDto(
            Long postId,
            String postImagePath,
            String recipeName,
            String introduction,
            Integer recipeTime,
            Long likedCounts
    ) {
        this.postId = postId;
        this.postImagePath = postImagePath;
        this.recipeName = recipeName;
        this.introduction = introduction;
        this.recipeTime = recipeTime;
        this.likedCounts = likedCounts;
    }
}
