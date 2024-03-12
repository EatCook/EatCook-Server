package com.itcook.cooking.api.domains.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PostSearchResponse {

    @Schema(description = "게시글 id")
    private Long postId;
    @Schema(description = "레시피 이름")
    private String recipeName;
    @Schema(description = "레시피 소개")
    private String introduction;
    @Schema(description = "대표 이미지")
    private String representImageFilePath;

    public static PostSearchResponse of(SearchResponse searchResponse) {
        return PostSearchResponse.builder()
            .postId(searchResponse.getPostId())
            .recipeName(searchResponse.getRecipeName())
            .introduction(searchResponse.getIntroduction())
            .representImageFilePath(searchResponse.getImageFilePath())
            .build();
    }
}
