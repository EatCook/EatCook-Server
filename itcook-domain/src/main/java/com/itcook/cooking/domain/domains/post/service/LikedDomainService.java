package com.itcook.cooking.domain.domains.post.service;

import com.itcook.cooking.domain.common.errorcode.LikedErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.domains.post.repository.LikedRepository;
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

    private final LikedRepository likedRepository;

    public List<Liked> getFindAllLiked(List<Long> postIdData) {
        return likedRepository.findAllByPostIdIn(postIdData);
    }

    public List<Liked> fetchFindByLikedUserId(Long userId) {
        List<Liked> findByLiked = likedRepository.findByItCookUserId(userId);

        if (findByLiked.isEmpty()) {
            throw new ApiException(LikedErrorCode.NOT_SAVED_IN_LIKED);
        }

        return findByLiked;
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
