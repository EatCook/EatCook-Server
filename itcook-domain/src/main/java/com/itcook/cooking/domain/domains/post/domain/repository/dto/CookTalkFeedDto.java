package com.itcook.cooking.domain.domains.post.domain.repository.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CookTalkFeedDto {

    private Long writerUserId;
    private String writerUserEmail;
    private Long postId;
    private String postImagePath;
    private String recipeName;
    private String introduction;
    private Long likeCounts;
    private Boolean likedCheck;

    @Builder
    public CookTalkFeedDto(
            Long writerUserId,
            String writerUserEmail,
            Long postId,
            String postImagePath,
            String recipeName,
            String introduction,
            Long likeCounts
    ) {
        this.writerUserId = writerUserId;
        this.writerUserEmail = writerUserEmail;
        this.postId = postId;
        this.postImagePath = postImagePath;
        this.recipeName = recipeName;
        this.introduction = introduction;
        this.likeCounts = likeCounts;
    }
}
