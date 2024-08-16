package com.itcook.cooking.domain.domains.post.domain.entity.dto;

import com.itcook.cooking.domain.domains.post.domain.enums.CookingType;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import lombok.Builder;

import java.util.List;

@Builder
public record RecipeAddDto(
        Long userId,
        String recipeName,
        Integer recipeTime,
        String introduction,
        String mainFileExtension,
        List<String> foodIngredients,
        List<CookingType> cookingType,
        List<LifeType> lifeTypes,
        List<RecipeProcessAddDto> recipeProcess

) {
    @Builder
    public record RecipeProcessAddDto(
            Integer stepNum,
            String recipeWriting,
            String fileExtension
    ) {
    }

}
