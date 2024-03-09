package com.itcook.cooking.domain.domains.post.service;

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
    public List<Liked> getFindLiked(Long postId) {
        return likedRepository.findAllByPostId(postId);
    }

}
