package com.itcook.cooking.domain.domains.post.helper;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.PostRepository;

public class PostServiceHelper {

    public static Post findExistingPostByIdAndPostFlag(PostRepository postRepository, Long postId) {
        return postRepository.findByIdAndPostFlag(postId, PostFlag.ACTIVATE)
                .orElseThrow(() -> new ApiException(PostErrorCode.POST_NOT_EXIST));
    }

    public static Post findExistingPostById(PostRepository postRepository, Long postId) {
        return postRepository.findById(postId)
            .orElseThrow(() -> new ApiException(PostErrorCode.POST_NOT_EXIST));
    }
}
