package com.itcook.cooking.domain.domains.post.domain.adaptor;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.domain.enums.CookingType;
import com.itcook.cooking.domain.domains.post.domain.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.domain.repository.HomePostQuerydslRepository;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.CookTalkFollowDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.HomeInterestDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.HomeSpecialDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.CookTalkFeedDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.RecipeDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.response.MyRecipeResponse;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.repository.PostQuerydslRepository;
import com.itcook.cooking.domain.domains.post.domain.repository.PostRepository;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import com.itcook.cooking.domain.domains.user.service.dto.response.OtherPagePostInfoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostAdaptor {

    private final PostRepository postRepository;
    private final PostQuerydslRepository postQuerydslRepository;
    private final HomePostQuerydslRepository homePostQuerydslRepository;

    public Page<CookTalkFeedDto> findCookTalkFeeds(Long authUserId, Pageable pageable) {
        return postQuerydslRepository.findCookTalkPostsWithLiked(authUserId, pageable);
    }

    public Page<CookTalkFollowDto> findCookTalkFollows(Long authUserId, List<Long> userId, Pageable pageable) {
        return postQuerydslRepository.findFollowPostWithLiked(authUserId, userId, pageable);
    }

    public Post queryPostByIdAndPostFlag(Long postId) {
        return postRepository.findByIdAndPostFlag(postId, PostFlag.ACTIVATE)
                .orElseThrow(() -> new ApiException(PostErrorCode.POST_NOT_EXIST));
    }

    public Page<MyRecipeResponse> findPostsWithLiked(Long userId, Pageable pageable) {
        return postQuerydslRepository.findPostsWithLiked(userId, pageable);
    }

    public Post findByIdOrElseThrow(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new ApiException(PostErrorCode.POST_NOT_EXIST));
    }

    public Page<HomeInterestDto> findPostsWithLikedAndArchiveDtoByCookingTheme(
            CookingType cookingTheme, Long userId, Pageable pageable) {
        return homePostQuerydslRepository.findPostsWithLikedAndArchiveDtoByCookingTheme(
                cookingTheme, userId, pageable);
    }

    public Page<HomeSpecialDto> findPostsWithLikedAndArchiveDtoByLifeTypeDefaultHealthDiet(
            LifeType lifeType, Long userId, Pageable pageable) {
        return homePostQuerydslRepository.findPostsWithLikedAndArchiveDtoByLifeTypeDefaultHealthDiet(
                lifeType, userId, pageable);
    }

    public Page<OtherPagePostInfoResponse> getOtherPagePostInfo(
            Long authUserId, Long otherUserId, Pageable pageable
    ) {
        return postQuerydslRepository.getOtherPagePostInfo(authUserId, otherUserId, pageable);
    }

<<<<<<< HEAD
    public void updatePostsDisabledBy(Long userId) {
        postRepository.updatePostToDisabled(userId);
=======
    public RecipeDto findRecipe(Long postId, Long userId) {
        return postQuerydslRepository.findRecipe(postId, userId);
>>>>>>> dev
    }
}
