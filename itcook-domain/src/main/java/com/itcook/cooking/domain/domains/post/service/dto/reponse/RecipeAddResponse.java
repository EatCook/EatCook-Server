package com.itcook.cooking.domain.domains.post.service.dto.reponse;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class RecipeAddResponse {

    private Long postId;
    private String mainPresignedUrl;

    private List<String> recipeProcessPresignedUrl;

    public static RecipeAddResponse of(Long postId, String mainUrl, List<String> subImageUrls) {
        return RecipeAddResponse.builder()
                .postId(postId)
                .mainPresignedUrl(mainUrl)
                .recipeProcessPresignedUrl(subImageUrls)
                .build();
    }

}
