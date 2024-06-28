package com.itcook.cooking.api.domains.post.dto.search;

import com.itcook.cooking.domain.domains.post.domain.repository.dto.SearchPostDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchPostResponse {


    @Schema(description = "게시글 id")
    private Long postId;
    @Schema(description = "레시피 이름")
    private String recipeName;
    @Schema(description = "레시피 소개")
    private String introduction;
    @Schema(description = "이미지 파일 위치")
    private String imageFilePath;
    @Schema(description = "좋아요 수")
    private Long likeCount;
    @Schema(description = "재료")
    private List<String> foodIngredients;
    @Schema(description = "유저 닉네임")
    private String userNickName;

    public static SearchPostResponse from(SearchPostDto searchPostDto, List<String> foodIngredients) {
        return SearchPostResponse.builder()
            .postId(searchPostDto.getPostId())
            .recipeName(searchPostDto.getRecipeName())
            .introduction(searchPostDto.getIntroduction())
            .likeCount(searchPostDto.getLikeCount())
            .imageFilePath(searchPostDto.getImageFilePath())
            .userNickName(searchPostDto.getUserNickName())
            .foodIngredients(foodIngredients)
            .build();

    }
}
