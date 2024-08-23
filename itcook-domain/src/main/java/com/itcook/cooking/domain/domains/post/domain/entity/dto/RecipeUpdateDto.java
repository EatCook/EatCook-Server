package com.itcook.cooking.domain.domains.post.domain.entity.dto;

import com.itcook.cooking.domain.domains.post.domain.enums.CookingType;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RecipeUpdateDto {
    private Long recipeId;
    private Long userId;
    private String recipeName;
    private Integer recipeTime;
    private String introduction;
    private String mainFileExtension;
    private List<String> foodIngredients;
    private List<CookingType> cookingType;
    private List<LifeType> lifeTypes;
    private List<RecipeProcessUpdateDto> recipeProcess;

    @Getter
    @Builder
    public static class RecipeProcessUpdateDto {
        private Integer stepNum;
        private String recipeWriting;
        private String fileExtension;

        public void updateRecipeProcessFileExtension(String fileExtension) {
            this.fileExtension = fileExtension;
        }
    }
}
