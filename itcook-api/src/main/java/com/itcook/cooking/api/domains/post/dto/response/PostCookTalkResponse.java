package com.itcook.cooking.api.domains.post.dto.response;

import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.repository.mapping.CookTalkUserMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "CookTalk response")
public class PostCookTalkResponse {

    @Schema(description = "레시피 id", example = "1")
    private Long id;
    @Schema(description = "제목", example = "김밥 만들기")
    private String recipeName;
    @Schema(description = "본문", example = "간단하게 만들 수 있어요")
    private String introduction;


    @Schema(description = "생성날짜", example = "2024-02-29T03:31:29.784088")
    private LocalDateTime createdAt;
    @Schema(description = "본문", example = "2024-02-29T03:31:29.784088")
    private LocalDateTime lastModifiedAt;

    @Schema(description = "유저 id", example = "1")
    private Long userId;
    @Schema(description = "유저 닉네임", example = "username")
    private String nickName;
    @Schema(description = "팔로우", example = "")
    private Boolean followChk;

    //팔로우 여부

    public static PostCookTalkResponse of(Post postAllData, CookTalkUserMapping userAllData, boolean check) {

        return PostCookTalkResponse.builder()
                .id(postAllData.getId())
                .recipeName(postAllData.getRecipeName())
                .introduction(postAllData.getIntroduction())
                .createdAt(postAllData.getCreatedAt())
                .lastModifiedAt(postAllData.getLastModifiedAt())
                .userId(postAllData.getUserId())
                .nickName(userAllData.getNickName())
                .followChk(check)
                .build();

    }

}
