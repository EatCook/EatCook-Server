package com.itcook.cooking.domain.domains.post.service;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.repository.LikedRepository;
import com.itcook.cooking.domain.domains.post.repository.dto.LikedDomainDto;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class LikedDomainService {

    private final LikedRepository likedRepository;

    public List<Liked> getFindAllLiked(List<Long> postIdData) {
        return likedRepository.findAllByPostIdIn(postIdData);
    }

    public LikedDomainDto fetchFindByLikedUserId(Long userId, Long postId) {
        LikedDomainDto userWithPostAndArchive = likedRepository.findUserWithPostAndArchiveById(userId, postId);

        nullCheckValidation(userWithPostAndArchive);

        return userWithPostAndArchive;
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
        likedRepository.save(liked);
    }

    @Transactional
    public void removeLiked(Long likedId) {
        likedRepository.deleteById(likedId);
    }
}
