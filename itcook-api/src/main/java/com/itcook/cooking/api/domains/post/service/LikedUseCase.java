package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.common.errorcode.LikedErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.service.LikedDomainService;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@UseCase
@RequiredArgsConstructor
@Slf4j
public class LikedUseCase {

    private final PostDomainService postDomainService;
    private final UserDomainService userDomainService;
    private final LikedDomainService likedDomainService;


    public void likedAdd(String email, Long postId) {
        ItCookUser findByItCookUser = userDomainService.fetchFindByEmail(email);

        Post findByPost = postDomainService.fetchFindByPost(postId).get();

        List<Liked> likedList = likedDomainService.fetchFindByLikedUserId(findByItCookUser.getId());
        boolean likedValid = false;
        for (Liked liked : likedList) {
            likedValid = Objects.equals(liked.getPostId(), findByPost.getId());
        }

        if (likedValid) {
            throw new ApiException(LikedErrorCode.ALREADY_ADD_Liked);
        }
        Liked newLiked = Liked.builder()
                .itCookUserId(findByItCookUser.getId())
                .postId(findByPost.getId())
                .build();
        likedDomainService.createLiked(newLiked);

    }

    public void likedDel(String email, Long postId) {
        ItCookUser findByItCookUser = userDomainService.fetchFindByEmail(email);

        Post findByPost = postDomainService.fetchFindByPost(postId).get();

        List<Liked> likedList = likedDomainService.fetchFindByLikedUserId(findByItCookUser.getId());

        boolean likedValid = false;
        Long likedId = null;

        for (Liked liked : likedList) {
            likedId = liked.getId();
            likedValid = Objects.equals(liked.getPostId(), findByPost.getId());
        }

        if (!likedValid) {
            throw new ApiException(LikedErrorCode.NOT_SAVED_IN_LIKED);
        }

        likedDomainService.removeLiked(likedId);
    }

}
