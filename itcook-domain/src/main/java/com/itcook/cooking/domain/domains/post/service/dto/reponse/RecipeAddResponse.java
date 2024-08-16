package com.itcook.cooking.domain.domains.post.service.dto.reponse;

import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import lombok.Builder;

import java.util.List;

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
