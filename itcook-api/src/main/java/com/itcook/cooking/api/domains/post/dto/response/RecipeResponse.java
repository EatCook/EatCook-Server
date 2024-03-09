package com.itcook.cooking.api.domains.post.dto.response;

import com.itcook.cooking.api.domains.post.dto.RecipeDto;
import com.itcook.cooking.api.domains.post.dto.RecipeProcessDto;
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
@Schema(name = "recipe response")
public class RecipeResponse {

    @Schema(description = "레시피 id", example = "1")
    private Long id;
    @Schema(description = "제목", example = "김밥 만들기")
    private String recipeName;
    @Schema(description = "조리 시간", example = "10")
    private Integer recipeTime;
    @Schema(description = "본문", example = "간단하게 만들 수 있어요")
    private String introduction;

    @Schema(description = "재료", example = "[\"김밥\",\"밥\"]")
    private List<String> foodIngredients;

    @Schema(description = "테마", example = "[\"한식\",\"중식\"]")
    private List<String> cookingType;

    @Schema(description = "조리 과정",
            example = "[\n {\n \"stepNum\": 1,\n \"recipeWriting\": \"밥을 준비해 주세요\",\n \"recipeProcessImagePath\": \"step1Image.jpeg\"\n},\n" +
                    "{\n \"stepNum\": 2,\n \"recipeWriting\": \"밥을 한 주먹 ~\",\n \"recipeProcessImagePath\": \"step2Image.jpeg\"\n}\n" +
                    "  ]")
    private List<RecipeProcessDto> recipeProcess;

    @Schema(description = "생성 날짜", example = "2024-02-29T03:31:29.784088")
    private LocalDateTime createdAt;
    @Schema(description = "마지막 수정 날짜", example = "2024-02-29T03:31:29.784088")
    private LocalDateTime lastModifiedAt;

    @Schema(description = "유저 id", example = "1")
    private Long userId;
    @Schema(description = "유저 닉네임", example = "username")
    private String nickName;

    @Schema(description = "팔로우 수", example = "10")
    private Integer followerCount;
    @Schema(description = "좋아요 수", example = "10")
    private Integer likedCount;

    @Schema(description = "팔로우 여부", example = "true")
    private Boolean followCheck;
    @Schema(description = "좋아요 여부", example = "true")
    private Boolean lickedCheck;
    @Schema(description = "보관함 여부", example = "true")
    private Boolean archiveCheck;

    public static RecipeResponse of(RecipeDto recipeDto) {
        return RecipeResponse.builder()
                .id(recipeDto.getId())
                .recipeName(recipeDto.getRecipeName())
                .recipeTime(recipeDto.getRecipeTime())
                .introduction(recipeDto.getIntroduction())
                .foodIngredients(recipeDto.getFoodIngredients())
                .cookingType(recipeDto.getCookingType())
                .recipeProcess(recipeDto.getRecipeProcess())
                .createdAt(recipeDto.getCreatedAt())
                .lastModifiedAt(recipeDto.getLastModifiedAt())
                .userId(recipeDto.getUserId())
                .nickName(recipeDto.getNickName())
                .followerCount(recipeDto.getFollowerCount())
                .likedCount(recipeDto.getLikedCount())
                .followCheck(recipeDto.getFollowCheck())
                .lickedCheck(recipeDto.getLickedCheck())
                .archiveCheck(recipeDto.getArchiveCheck())
                .build();
    }
}
