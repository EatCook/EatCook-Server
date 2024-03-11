package com.itcook.cooking.domain.domains.post.dto.response;

import com.itcook.cooking.domain.domains.post.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResponse {

    private Long postId;
    private String recipeName;
    private String introduction;
    private String representImageFilePath;

    public static SearchResponse of(Post post) {
        return SearchResponse.builder()
            .postId(post.getId())
            .recipeName(post.getRecipeName())
            .introduction(post.getIntroduction())
            .build();
    }

}
