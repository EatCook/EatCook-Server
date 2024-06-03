package com.itcook.cooking.domain.domains.post.service;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.adaptor.LikedAdaptor;
import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.domains.post.repository.LikedRepository;
import com.itcook.cooking.domain.domains.post.repository.dto.LikedDomainDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LikedDomainService {

    private final LikedAdaptor likedAdaptor;
    private final LikedRepository likedRepository;

    public List<Liked> getFindAllLiked(List<Long> postIdData) {
        return likedRepository.findAllByPostIdIn(postIdData);
    }

    public void validateDuplicateLiked(Long userId, Long postId) {
        likedAdaptor.checkDuplicateLiked(userId, postId);
    }

    public Liked validateEmptyArchive(Long userId, Long postId) {
        return likedAdaptor.validateEmptyLiked(userId, postId);
    }

    private void nullCheckValidation(LikedDomainDto userWithPostAndArchive) {
        if (userWithPostAndArchive.getItCookUser() == null) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        }

        if (userWithPostAndArchive.getPost() == null) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }
    }

    @Transactional
    public void createLiked(Liked liked) {
        likedAdaptor.saveLiked(liked);
    }

    @Transactional
    public void removeLiked(Liked liked) {
        likedAdaptor.removeLiked(liked);
    }
}
