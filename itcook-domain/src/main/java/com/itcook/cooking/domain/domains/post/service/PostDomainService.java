package com.itcook.cooking.domain.domains.post.service;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostDomainService {

    private final PostRepository postRepository;

    public List<Post> fetchFindAllByUserIdNotWithUsers(Long userId) {
        List<Post> findPostAllData = postRepository.findAllByUserIdNot(userId, Sort.by(Sort.Direction.DESC, "lastModifiedAt"));

        if (ObjectUtils.isEmpty(findPostAllData)) {
            throw new ApiException(PostErrorCode.POST_NOT_FOUND);
        }

        return findPostAllData;
    }

    // followingSelectAll
    public List<Post> fetchFindFollowingCookTalk(List<Long> userId) {
        List<Post> findFollowingCookTalkData = postRepository.findByUserIdIn(userId, Sort.by(Sort.Direction.DESC, "lastModifiedAt"));

        if (ObjectUtils.isEmpty(findFollowingCookTalkData)) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }

        return findFollowingCookTalkData;
    }

    public void fetchFindByMyRecipe(Long userId) {
        postRepository.findByUserId(userId);
    }

}
