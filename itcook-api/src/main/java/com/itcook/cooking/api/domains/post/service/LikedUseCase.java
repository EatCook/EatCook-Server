package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.like.domain.entity.Liked;
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

        likedService.validateDuplicateLiked(findByItCookUser.getId(), reqPostId);

        Liked newLiked = Liked.builder()
                .postId(findByPost.getId()) // 좋아요 누른 게시글 id
                .itCookUserId(findByItCookUser.getId()) // from user
                .build();

        likedService.createLiked(newLiked);
    }

    public void likedDel(String email, Long reqPostId) {
        ItCookUser findByItCookUser = userService.findUserByEmail(email);
        Post findByPost = postService.fetchFindByPost(reqPostId);

        Liked findLiked = likedService.validateEmptyArchive(findByItCookUser.getId(), findByPost.getId());

        likedService.removeLiked(findLiked);
    }

}
