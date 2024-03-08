package com.itcook.cooking.api.domains.post.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@Schema(description = "recipe process dto")
public class RecipeProcessDto {

    private Integer stepNum;

    private String recipeWriting;

    private String recipeProcessImagePath;

    public RecipeProcessDto(Integer stepNum, String recipeWriting, String recipeProcessImagePath) {
        this.stepNum = stepNum;
        this.recipeWriting = recipeWriting;
        this.recipeProcessImagePath = recipeProcessImagePath;
    }
}
