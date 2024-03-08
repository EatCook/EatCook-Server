package com.itcook.cooking.api.domains.post.dto.response;

import com.itcook.cooking.api.domains.post.dto.CookTalkDto;
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
@Schema(description = "cooktalk response")
public class CookTalkResponse {

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

    public static CookTalkResponse of(CookTalkDto cookTalkDto) {
        return CookTalkResponse.builder()
                .id(cookTalkDto.getId())
                .recipeName(cookTalkDto.getRecipeName())
                .introduction(cookTalkDto.getIntroduction())
                .createdAt(cookTalkDto.getCreatedAt())
                .lastModifiedAt(cookTalkDto.getLastModifiedAt())
                .userId(cookTalkDto.getUserId())
                .nickName(cookTalkDto.getNickName())
                .followChk(cookTalkDto.getFollowChk())
                .build();
    }

}
