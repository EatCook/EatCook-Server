package com.itcook.cooking.domain.domains.like.repository.dto;

import com.itcook.cooking.domain.domains.like.entity.Liked;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LikedDomainDto {

    private Post post;
    private ItCookUser itCookUser;
    private Liked liked;

}
