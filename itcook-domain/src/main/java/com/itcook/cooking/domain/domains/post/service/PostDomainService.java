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
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostDomainService {

    private final PostRepository postRepository;

    public List<Post> fetchFindAllByUserIdNotWithUsers(Long userId) {
        List<Post> findPostAllData = postRepository.findAllByUserIdNot(userId, Sort.by(Sort.Direction.DESC, "lastModifiedAt"));

        if (ObjectUtils.isEmpty(findPostAllData)) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }

        return findPostAllData;
    }

    public List<Post> fetchFindFollowingCookTalk(List<Long> userId) {
        List<Post> findFollowingCookTalkData = postRepository.findByUserIdIn(userId, Sort.by(Sort.Direction.DESC, "lastModifiedAt"));

        if (ObjectUtils.isEmpty(findFollowingCookTalkData)) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }

        return findFollowingCookTalkData;
    }
    public Optional<Post> fetchFindPost(Long userId) {
        Optional<Post> findPostData = postRepository.findById(userId);

        if (ObjectUtils.isEmpty(findPostData)) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }

        return findPostData;
    }

    public Post fetchFindByMyPost(Long userId) {
        return postRepository.findById(userId).orElse(null);
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Post postUpdateData) {
        Post postEntityData = postRepository.findById(postUpdateData.getId()).orElse(null);

        if (postEntityData == null) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }

        postEntityData.updatePost(postUpdateData);
        return postUpdateData;
    }

    public void deletePost(Long postId) {
        Post postEntity = postRepository.findById(postId).orElse(null);

        if (postEntity == null) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }

        postEntity.deletePost();
    }
}
