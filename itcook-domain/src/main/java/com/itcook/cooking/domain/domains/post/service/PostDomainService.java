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

    public List<Post> findPostAllData() {
        List<Post> findPostAll = postRepository.findAll(Sort.by(Sort.Direction.DESC, "createdAt"));

        if (ObjectUtils.isEmpty(findPostAll)) {
            throw new ApiException(PostErrorCode.POST_NOT_FOUND);
        }

        return findPostAll;
    }
/*
    // followingSelectAll
    public List<Post> fetchFollowingCookTalkData(List<Long> userId) {
        log.info("fetchFollowingCookTalkData 접근함 = {}", userId);
        List<Post> findFollowingCookTalk = postRepository.findByUserIdIn(userId);
        log.info("findFollowingCookTalk 접근함 = {}", findFollowingCookTalk.size());

        if (ObjectUtils.isEmpty(findFollowingCookTalk)) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }

        return findFollowingCookTalk;
    }
 */

}
