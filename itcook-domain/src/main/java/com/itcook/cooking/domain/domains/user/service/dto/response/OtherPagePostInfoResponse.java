package com.itcook.cooking.domain.domains.user.service.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OtherPagePostInfoResponse {

    private Long writerUserId;
    private String writerUserEmail;
    private String writerProfile;
    private String writerNickName;
    private Long postId;
    private String introduction;
    private String postImagePath;
    private String recipeName;
    private Integer recipeTime;
    private Long likedCounts;
    private Boolean likedCheck;
    private Boolean archiveCheck;

    public OtherPagePostInfoResponse(
            Long writerUserId,
            String writerUserEmail,
            String writerProfile,
            String writerNickName,
            Long postId,
            String introduction,
            String postImagePath,
            String recipeName,
            Integer recipeTime,
            Long likedCounts
    ) {
        this.writerUserId = writerUserId;
        this.writerUserEmail = writerUserEmail;
        this.writerProfile = writerProfile;
        this.writerNickName = writerNickName;
        this.postId = postId;
        this.introduction = introduction;
        this.postImagePath = postImagePath;
        this.recipeName = recipeName;
        this.recipeTime = recipeTime;
        this.likedCounts = likedCounts;
    }
}
