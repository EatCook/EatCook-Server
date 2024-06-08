package com.itcook.cooking.domain.domains.like.service;

import com.itcook.cooking.domain.domains.like.entity.Liked;
import com.itcook.cooking.domain.domains.like.repository.LikedRepository;
import com.itcook.cooking.domain.domains.like.adaptor.LikedAdaptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void createLiked(Liked liked) {
        likedAdaptor.saveLiked(liked);
    }

    @Transactional
    public void removeLiked(Liked liked) {
        likedAdaptor.removeLiked(liked);
    }
}
