package com.itcook.cooking.domain.domains.like.domain.adaptor;

import com.itcook.cooking.domain.common.errorcode.LikedErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.like.domain.entity.Liked;
import com.itcook.cooking.domain.domains.like.domain.repository.LikedRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LikedAdaptor {
    private final LikedRepository likedRepository;

    public void checkDuplicateLiked(Long userId, Long postId) {
        likedRepository.findByItCookUserIdAndPostId(userId, postId)
                .ifPresent(it -> {
                    throw new ApiException(LikedErrorCode.ALREADY_ADD_Liked);
                });
    }

    public Liked validateEmptyLiked(Long userId, Long postId) {
        return likedRepository.findByItCookUserIdAndPostId(userId, postId).orElseThrow(() ->
                new ApiException(LikedErrorCode.NOT_SAVED_IN_LIKED)
        );
    }

    public void saveLiked(Liked liked) {
        likedRepository.save(liked);
    }

    public void removeLiked(Liked liked) {
        likedRepository.delete(liked);
    }

}
