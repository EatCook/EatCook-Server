package com.itcook.cooking.api.domains.post.dto.recipe;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@Builder
public class RecipeProcessDto {

    private Integer stepNum;

    private String recipeWriting;

    private String fileExtension;

    public RecipeProcessDto(Integer stepNum, String recipeWriting, String fileExtension) {
        this.stepNum = stepNum;
        this.recipeWriting = recipeWriting;
        this.fileExtension = fileExtension;
    }

}
