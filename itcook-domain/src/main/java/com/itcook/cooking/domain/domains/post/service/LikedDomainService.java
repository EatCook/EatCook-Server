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
        List<Object[]> userWithPostAndArchive = likedRepository.findUserWithPostAndArchiveById(userId, postId);

        nullCheckValidation(userWithPostAndArchive);

        return LikedDomainDto.builder()
                .itCookUser((ItCookUser) userWithPostAndArchive.get(0)[0])
                .post((Post) userWithPostAndArchive.get(0)[1])
                .liked((Liked) userWithPostAndArchive.get(0)[2])
                .build();
    }

    private void nullCheckValidation(List<Object[]> userWithPostAndArchive) {
        if (userWithPostAndArchive.get(0)[0] == null) {
            throw new ApiException(UserErrorCode.USER_NOT_FOUND);
        }

        if (userWithPostAndArchive.get(0)[1] == null) {
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
