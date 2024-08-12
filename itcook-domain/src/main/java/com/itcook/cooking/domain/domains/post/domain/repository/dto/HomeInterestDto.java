package com.itcook.cooking.domain.domains.post.domain.repository.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class HomeInterestDto {
    private Long postId;
    private String postImagePath;
    private String recipeName;
    private Integer recipeTime;
    private String profile;
    private String nickName;
    private Long likedCounts;
    private Boolean likedCheck;
    private Boolean archiveCheck;

    public HomeInterestDto(
            Long postId, String postImagePath, String recipeName,
            Integer recipeTime, String profile, String nickName,
            Long likedCounts) {
        this.postId = postId;
        this.postImagePath = postImagePath;
        this.recipeName = recipeName;
        this.recipeTime = recipeTime;
        this.profile = profile;
        this.nickName = nickName;
        this.likedCounts = likedCounts;
    }
}
