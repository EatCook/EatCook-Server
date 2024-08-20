package com.itcook.cooking.api.domains.post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RecipeUpdateResponse {
    private String mainPresignedUrl;

    private List<String> recipeProcessPresignedUrl;
}
