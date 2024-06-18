package com.itcook.cooking.domain.domains.post.repository.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HomeSpecialDto {
    private Long postId;
    private String postImagePath;
    private String recipeName;
    private Integer recipeTime;
    private Long likedCounts;
    private Boolean likedCheck;
    private Boolean archiveCheck;

    public HomeSpecialDto(
            Long postId, String postImagePath, String recipeName,
            Integer recipeTime, Long likedCounts) {
        this.postId = postId;
        this.postImagePath = postImagePath;
        this.recipeName = recipeName;
        this.recipeTime = recipeTime;
        this.likedCounts = likedCounts;
    }
}
