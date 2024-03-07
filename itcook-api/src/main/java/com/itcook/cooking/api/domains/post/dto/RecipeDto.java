package com.itcook.cooking.api.domains.post.dto;

import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.entity.PostCookingTheme;
import com.itcook.cooking.domain.domains.post.entity.RecipeProcess;
import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.repository.mapping.CookTalkUserMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "recipe dto")
public class RecipeDto {

    @Schema(description = "레시피 id", example = "1")
    private Long id;
    @Schema(description = "제목", example = "김밥 만들기")
    private String recipeName;
    @Schema(description = "조리 시간", example = "10")
    private Integer recipeTime;
    @Schema(description = "본문", example = "간단하게 만들 수 있어요")
    private String introduction;

    @Schema(description = "생성 날짜", example = "2024-02-29T03:31:29.784088")
    private LocalDateTime createdAt;
    @Schema(description = "마지막 수정 날짜", example = "2024-02-29T03:31:29.784088")
    private LocalDateTime lastModifiedAt;

    @Schema(description = "재료수", example = "2")
    private Integer foodIngredientsCnt;
    @Schema(description = "재료", example = "[\"김밥\",\"밥\"]")
    private List<String> foodIngredients;

    @Schema(description = "테마", example = "[\"한식\",\"중식\"]")
    private List<String> cookingType;

    @Schema(description = "조리 과정", example = "{\"1\": \"밥을 준비해 주세요\",\"2\": \"밥을 한 주먹 ~\"}")
    private Map<Integer, String> recipeProcess;

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

    public RecipeDto(Post post, CookTalkUserMapping user) {
        this.id = post.getId();
        this.recipeName = post.getRecipeName();
        this.recipeTime = post.getRecipeTime();
        this.introduction = post.getIntroduction();
        this.createdAt = post.getCreatedAt();
        this.lastModifiedAt = post.getLastModifiedAt();
        this.userId = post.getUserId();
        this.nickName = user.getNickName();
        this.followerCount = user.getFollow().size();
        this.foodIngredientsCnt = post.getFoodIngredients().size();
        this.foodIngredients = post.getFoodIngredients();
    }

    public void toRecipeDto(List<RecipeProcess> findRecipeProcesses, List<PostCookingTheme> findAllPostCookingTheme, List<ItCookUser> liked, boolean likedValidation, boolean archiveValidation) {
        this.recipeProcess = findRecipeProcesses.stream()
                .collect(Collectors.toMap(RecipeProcess::getStepNum, RecipeProcess::getRecipeWriting));

        this.cookingType = findAllPostCookingTheme.stream().map(PostCookingTheme::getCookingType)
                .map(CookingType::getCookingTypeName)
                .collect(Collectors.toList());
        this.likedCount = liked.size();
        this.lickedCheck = likedValidation;
        this.archiveCheck = archiveValidation;
    }
}
