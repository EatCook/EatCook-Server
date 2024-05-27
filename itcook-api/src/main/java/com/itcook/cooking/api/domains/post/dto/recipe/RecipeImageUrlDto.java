package com.itcook.cooking.api.domains.post.dto.recipe;

import com.itcook.cooking.domain.infra.s3.ImageUrlDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecipeImageUrlDto {

    private ImageUrlDto mainImageUrl;

    private List<ImageUrlDto> recipeProcessImageUrl;
}
