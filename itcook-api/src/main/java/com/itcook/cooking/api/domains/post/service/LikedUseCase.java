package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.like.service.LikedService;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.service.PostService;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class LikedUseCase {

    private final UserService userService;
    private final PostService postService;
    private final LikedService likedService;

    @Transactional
    public void likedAdd(String email, Long reqPostId) {
        ItCookUser findByItCookUser = userService.findUserByEmail(email);
        Post findByPost = postService.fetchFindByPost(reqPostId);

        likedService.createLiked(findByItCookUser.getId(), findByPost.getId());
    }

    public void removeLiked(String email, Long reqPostId) {
        ItCookUser findByItCookUser = userService.findUserByEmail(email);
        Post findByPost = postService.fetchFindByPost(reqPostId);

        likedService.removeLiked(findByItCookUser.getId(), findByPost.getId());
    }

}
