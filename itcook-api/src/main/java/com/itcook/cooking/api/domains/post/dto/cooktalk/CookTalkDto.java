package com.itcook.cooking.api.domains.post.dto.cooktalk;

import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.user.repository.mapping.CookTalkUserMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CookTalkDto {

    private Long id;
    private String recipeName;
    private String introduction;
    private String postImagePath;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    private Long userId;
    private String nickName;
    private Boolean followChk;

    public CookTalkDto(Post post, CookTalkUserMapping user) {
        this.id = post.getId();
        this.recipeName = post.getRecipeName();
        this.introduction = post.getIntroduction();
        this.postImagePath = post.getPostImagePath();
        this.createdAt = post.getCreatedAt();
        this.lastModifiedAt = post.getLastModifiedAt();
        this.userId = post.getUserId();
        this.nickName = user.getNickName();
    }
}
