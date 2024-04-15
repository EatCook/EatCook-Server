package com.itcook.cooking.api.domains.post.dto.recipe;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Builder
public class RecipeProcessReadDto {

    @NotNull(message = "조리 과정 번호가 없습니다.")
    private Integer stepNum;
    @NotNull(message = "조리 과정 설명이 없습니다.")
    private String recipeWriting;

    private String recipeProcessImagePath;

    public RecipeProcessReadDto(Integer stepNum, String recipeWriting, String recipeProcessImagePath) {
        this.stepNum = stepNum;
        this.recipeWriting = recipeWriting;
        this.recipeProcessImagePath = recipeProcessImagePath;
    }
}
