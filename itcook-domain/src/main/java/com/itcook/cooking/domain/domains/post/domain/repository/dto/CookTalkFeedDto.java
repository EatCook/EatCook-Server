package com.itcook.cooking.domain.domains.post.domain.repository.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
    private LocalDateTime lastModifiedAt;
    private String profile;
    private String nickName;

    @Builder
    public CookTalkFeedDto(
            Long writerUserId,
            String writerUserEmail,
            Long postId,
            String postImagePath,
            String recipeName,
            String introduction,
            LocalDateTime lastModifiedAt,
            Long likeCounts,
            String profile,
            String nickName
    ) {
        this.writerUserId = writerUserId;
        this.writerUserEmail = writerUserEmail;
        this.postId = postId;
        this.postImagePath = postImagePath;
        this.recipeName = recipeName;
        this.introduction = introduction;
        this.lastModifiedAt = lastModifiedAt;
        this.likeCounts = likeCounts;
        this.profile = profile;
        this.nickName = nickName;
    }
}
