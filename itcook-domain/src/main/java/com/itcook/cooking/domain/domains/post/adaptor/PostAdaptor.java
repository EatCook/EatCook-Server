package com.itcook.cooking.domain.domains.post.adaptor;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.PostQuerydslRepository;
import com.itcook.cooking.domain.domains.post.repository.PostRepository;
import com.itcook.cooking.domain.domains.post.repository.dto.PostWithLikedDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostAdaptor {

    private final PostRepository postRepository;
    private final PostQuerydslRepository postQuerydslRepository;

    public Post queryPostByIdAndPostFlag(Long postId) {
        return postRepository.findByIdAndPostFlag(postId, PostFlag.ACTIVATE)
            .orElseThrow(() -> new ApiException(PostErrorCode.POST_NOT_EXIST));
    }

    public Page<PostWithLikedDto> findPostsWithLiked(Long userId, Pageable pageable) {
        return postQuerydslRepository.findPostsWithLiked(userId, pageable);
    }

    public Post findByIdOrElseThrow(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new ApiException(PostErrorCode.POST_NOT_EXIST));
    }
}
