package com.itcook.cooking.domain.domains.like.service;

import com.itcook.cooking.domain.domains.like.domain.adaptor.LikedAdaptor;
import com.itcook.cooking.domain.domains.like.domain.entity.Liked;
import com.itcook.cooking.domain.domains.like.domain.entity.validator.LikedValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LikedService {

    private final LikedAdaptor likedAdaptor;
    private final LikedValidator likedValidate;

    @Transactional
    public void createLiked(Long userId, Long postId) {
        likedAdaptor.checkDuplicateLiked(userId, postId);
        Liked liked = Liked.addLiked(userId, postId, likedValidate);
        likedAdaptor.saveLiked(liked);
    }

    @Transactional
    public void removeLiked(Long userId, Long postId) {
        Liked findLiked = likedAdaptor.validateEmptyLiked(userId, postId);

        likedAdaptor.removeLiked(findLiked);
    }

}
