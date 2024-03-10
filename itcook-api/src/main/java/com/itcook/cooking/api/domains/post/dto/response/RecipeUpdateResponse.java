package com.itcook.cooking.api.domains.post.dto.response;

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
@Schema(name = "recipe update response")
public class RecipeUpdateResponse {
    private String mainPresignedUrl;

    private List<String> recipeProcessPresignedUrl;
}
