package com.itcook.cooking.api.domains.post.dto.response;

import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "게시글 검색 응답")
public class SearchResponse {

    @Schema(description = "게시글 id")
    private Long postId;
    @Schema(description = "레시피 이름")
    private String recipeName;
    @Schema(description = "레시피 소개")
    private String introduction;
    @Schema(description = "이미지 파일 위치")
    private String imageFilePath;

    public static SearchResponse of(Post post) {
        return SearchResponse.builder()
            .postId(post.getId())
            .recipeName(post.getRecipeName())
            .introduction(post.getIntroduction())
            .imageFilePath(post.getPostImagePath())
            .build();
    }

}
