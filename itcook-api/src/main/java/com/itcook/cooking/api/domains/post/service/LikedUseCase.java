package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.common.events.user.UserLikedEvent;
import com.itcook.cooking.domain.domains.like.entity.Liked;
import com.itcook.cooking.domain.domains.like.service.LikedService;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.service.PostService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class LikedUseCase {

    private final UserService userService;
    private final PostService postService;
    private final LikedService likedService;
    private final ApplicationEventPublisher eventPublisher;


    public void likedAdd(String email, Long reqPostId) {
        ItCookUser findByItCookUser = userService.findUserByEmail(email);
        Post findByPost = postService.fetchFindByPost(reqPostId);

        likedService.validateDuplicateLiked(findByItCookUser.getId(), reqPostId);

        Liked newLiked = Liked.builder()
                .postId(findByPost.getId())
                .itCookUserId(findByItCookUser.getId())
                .build();

        likedService.createLiked(newLiked);

        // 게시물 작성자에게 좋아요 요청 알림
        eventPublisher.publishEvent(UserLikedEvent.of(findByItCookUser.getNickName(), reqPostId,
                findByItCookUser.getId()));
    }

    public void likedDel(String email, Long reqPostId) {
        ItCookUser findByItCookUser = userService.findUserByEmail(email);
        Post findByPost = postService.fetchFindByPost(reqPostId);

        Liked findLiked = likedService.validateEmptyArchive(findByItCookUser.getId(), findByPost.getId());

        likedService.removeLiked(findLiked);
    }

}
