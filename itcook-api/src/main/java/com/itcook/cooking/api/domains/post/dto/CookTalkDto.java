package com.itcook.cooking.api.domains.post.dto;

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
@Schema(description = "cooktalk feed response")
public class CookTalkDto {

    @Schema(description = "레시피 id", example = "1")
    private Long id;
    @Schema(description = "제목", example = "김밥 만들기")
    private String recipeName;
    @Schema(description = "본문", example = "간단하게 만들 수 있어요")
    private String introduction;

    @Schema(description = "생성 날짜", example = "2024-02-29T03:31:29.784088")
    private LocalDateTime createdAt;
    @Schema(description = "마지막 수정 날짜", example = "2024-02-29T03:31:29.784088")
    private LocalDateTime lastModifiedAt;

    @Schema(description = "유저 id", example = "1")
    private Long userId;
    @Schema(description = "유저 닉네임", example = "username")
    private String nickName;
    @Schema(description = "팔로우 여부", example = "true")
    private Boolean followChk;

    public CookTalkDto(Post post, CookTalkUserMapping user) {
        this.id = post.getId();
        this.recipeName = post.getRecipeName();
        this.introduction = post.getIntroduction();
        this.createdAt = post.getCreatedAt();
        this.lastModifiedAt = post.getLastModifiedAt();
        this.userId = post.getUserId();
        this.nickName = user.getNickName();
    }
}
