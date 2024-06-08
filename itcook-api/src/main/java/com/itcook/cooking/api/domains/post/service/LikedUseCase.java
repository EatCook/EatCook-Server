package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.common.events.user.UserLikedEvent;
import com.itcook.cooking.domain.domains.like.entity.Liked;
import com.itcook.cooking.domain.domains.like.service.LikedDomainService;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class LikedUseCase {

    private final UserDomainService userDomainService;
    private final PostDomainService postDomainService;
    private final LikedDomainService likedDomainService;
    private final ApplicationEventPublisher eventPublisher;


    public void likedAdd(String email, Long reqPostId) {
        ItCookUser findByItCookUser = userDomainService.findUserByEmail(email);
        Post findByPost = postDomainService.fetchFindByPost(reqPostId);

        likedDomainService.validateDuplicateLiked(findByItCookUser.getId(), reqPostId);

        Liked newLiked = Liked.builder()
                .postId(findByPost.getId())
                .itCookUserId(findByItCookUser.getId())
                .build();

        likedDomainService.createLiked(newLiked);

        // 게시물 작성자에게 좋아요 요청 알림
        eventPublisher.publishEvent(UserLikedEvent.of(findByItCookUser.getNickName(), reqPostId,
                findByItCookUser.getId()));
    }

    public void likedDel(String email, Long reqPostId) {
        ItCookUser findByItCookUser = userDomainService.findUserByEmail(email);
        Post findByPost = postDomainService.fetchFindByPost(reqPostId);

        Liked findLiked = likedDomainService.validateEmptyArchive(findByItCookUser.getId(), findByPost.getId());

        likedDomainService.removeLiked(findLiked);
    }

}
