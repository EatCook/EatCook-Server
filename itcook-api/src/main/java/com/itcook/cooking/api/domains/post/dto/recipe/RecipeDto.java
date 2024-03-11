package com.itcook.cooking.api.domains.post.dto.recipe;

import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.entity.PostCookingTheme;
import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.user.repository.mapping.CookTalkUserMapping;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeDto {

    private Long id;
    private String recipeName;
    private Integer recipeTime;
    private String introduction;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    private Integer foodIngredientsCnt;
    private List<String> foodIngredients;

    private List<String> cookingType;

    private List<RecipeProcessDto> recipeProcess;

    private Long userId;
    private String nickName;

    private Integer followerCount;
    private Integer likedCount;

    private Boolean followCheck;
    private Boolean lickedCheck;
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

    public void toRecipeDto(List<RecipeProcessDto> findRecipeProcesses, List<PostCookingTheme> findAllPostCookingTheme, List<Liked> findAllLiked, boolean likedValidation, boolean archiveValidation) {
        this.cookingType = findAllPostCookingTheme.stream().map(PostCookingTheme::getCookingType)
                .map(CookingType::getCookingTypeName)
                .collect(Collectors.toList());

        this.likedCount = findAllLiked.size();
        this.lickedCheck = likedValidation;
        this.archiveCheck = archiveValidation;
        this.recipeProcess = findRecipeProcesses;
    }

}
