package com.itcook.cooking.api.domains.post.dto.recipe;

import com.itcook.cooking.infra.s3.ImageUrlDto;
import io.swagger.v3.oas.annotations.media.Schema;
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
