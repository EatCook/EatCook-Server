package com.itcook.cooking.domain.domains.like.domain.repository.dto;

import com.itcook.cooking.domain.domains.like.domain.entity.Liked;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
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
