package com.itcook.cooking.api.domains.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "recipe create response")
public class RecipeCreateResponse {

    private Long postId;
    private String mainPresignedUrl;

    private List<String> recipeProcessPresignedUrl;
}
