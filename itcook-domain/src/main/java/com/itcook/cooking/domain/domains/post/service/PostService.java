package com.itcook.cooking.domain.domains.post.service;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.adaptor.PostAdaptor;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.PostRepository;
import com.itcook.cooking.domain.domains.post.repository.dto.PostWithLikedDto;
import com.itcook.cooking.domain.infra.s3.ImageUrlDto;
import java.util.List;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostAdaptor postAdaptor;

    public Page<Object[]> fetchFindAllByCookTalkFeedV2(Long userId, Pageable pageable) {
        Page<Object[]> findPostAllData = postRepository.findAllByUserIdNotAndPostFlag(userId, PostFlag.ACTIVATE, pageable);

//        if (!findPostAllData.hasContent()) {
//            throw new ApiException(PostErrorCode.POST_200_NOT_EXIST);
//        }

        return findPostAllData;
    }

    public Page<Object[]> fetchFindFollowingCookTalk(List<Long> userId, Pageable pageable) {
        Page<Object[]> findFollowingCookTalkData = postRepository.findByUserIdInAndPostFlag(userId, PostFlag.ACTIVATE, pageable);

//        if (!findFollowingCookTalkData.hasContent()) {
//            throw new ApiException(PostErrorCode.POST_200_NOT_EXIST);
//        }

        return findFollowingCookTalkData;
    }

    public List<Object[]> fetchFindByRecipe(Long postId) {
        List<Object[]> findPostData = postRepository.findRecipeData(postId, PostFlag.ACTIVATE);

        if (findPostData.isEmpty()) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }

        return findPostData;
    }

    public Page<PostWithLikedDto> getPostsByUserId(Long userId, Pageable pageable) {
        return postAdaptor.findPostsWithLiked(userId, pageable);
    }

    public Post fetchFindByPost(Long postId) {
        return postAdaptor.findByIdOrElseThrow(postId);
    }

    public Post createPost(Post post) {
        return postRepository.save(post);
    }

    public Post updatePost(Post postUpdateData, ImageUrlDto mainImageUrlDto) {
        Post postEntityData = postRepository.findById(postUpdateData.getId()).orElse(null);

        if (postEntityData == null) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }

        if (mainImageUrlDto != null) {
            postUpdateData.updateFileExtension(mainImageUrlDto.getKey());
        } else {
            postUpdateData.updateFileExtension(postEntityData.getPostImagePath());
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

//    public List<Post> searchByRecipeNameOrIngredients(
//            Long lastId, List<String> names, Integer size
//    ) {
//        return postQuerydslRepository.findNamesWithPagination(lastId, names, size);
//    }


}
