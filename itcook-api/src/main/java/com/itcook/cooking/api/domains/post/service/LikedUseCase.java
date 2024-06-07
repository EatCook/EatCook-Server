package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.common.errorcode.LikedErrorCode;
import com.itcook.cooking.domain.common.events.user.UserLikedEvent;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.like.entity.Liked;
import com.itcook.cooking.domain.domains.like.repository.dto.LikedDomainDto;
import com.itcook.cooking.domain.domains.like.service.LikedDomainService;
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
    private final LikedDomainService likedDomainService;
    private final ApplicationEventPublisher eventPublisher;


    public void likedAdd(String email, Long reqPostId) {
        ItCookUser findByItCookUser = userDomainService.findUserByEmail(email);

        LikedDomainDto likedDomainDto = likedDomainService.fetchFindByLikedUserId(
            findByItCookUser.getId(), reqPostId);

        if (likedDomainDto.getLiked() != null) {
            throw new ApiException(LikedErrorCode.ALREADY_ADD_Liked);
        }

        likedDomainService.createLiked(
            Liked.builder()
                .postId(likedDomainDto.getPost().getId())
                .itCookUserId(likedDomainDto.getItCookUser().getId())
                .build()
        );

        // 게시물 작성자에게 좋아요 요청 알림
        eventPublisher.publishEvent(UserLikedEvent.of(findByItCookUser.getNickName(), reqPostId,
            likedDomainDto.getItCookUser().getId()));
    }

    public void likedDel(String email, Long reqPostId) {
        ItCookUser findByItCookUser = userDomainService.findUserByEmail(email);

        LikedDomainDto likedDomainDto = likedDomainService.fetchFindByLikedUserId(
            findByItCookUser.getId(), reqPostId);

        if (likedDomainDto.getLiked() == null) {
            throw new ApiException(LikedErrorCode.NOT_SAVED_IN_LIKED);
        }

        likedDomainService.removeLiked(likedDomainDto.getLiked().getId());
    }

}
