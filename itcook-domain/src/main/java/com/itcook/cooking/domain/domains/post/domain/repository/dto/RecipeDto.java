package com.itcook.cooking.domain.domains.post.domain.repository.dto;

import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecipeDto {
    private Post post;
    private ItCookUser itCookUser;
    private Long likedCount;
    private Boolean likedCheck;
    private Boolean archiveCheck;

    public RecipeDto(
            Post post,
            ItCookUser itCookUser,
            Long likedCount
    ) {
        this.post = post;
        this.itCookUser = itCookUser;
        this.likedCount = likedCount;
    }
}
