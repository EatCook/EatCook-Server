package com.itcook.cooking.domain.domains.post.service;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.infra.s3.ImageUrlDto;
import com.itcook.cooking.domain.domains.post.domain.adaptor.PostAdaptor;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.enums.CookingType;
import com.itcook.cooking.domain.domains.post.domain.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.domain.repository.PostRepository;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.CookTalkFeedDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.CookTalkFollowDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.HomeInterestDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.HomeSpecialDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.RecipeDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.response.MyRecipeResponse;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import com.itcook.cooking.domain.domains.user.service.dto.response.OtherPagePostInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostAdaptor postAdaptor;

    public Page<CookTalkFeedDto> getCookTalkFeeds(Long authUserId, Pageable pageable) {
        return postAdaptor.findCookTalkFeeds(authUserId, pageable);
    }

    public Page<CookTalkFollowDto> getCookTalkFollows(Long authUserId, List<Long> followIds, Pageable pageable) {
        return postAdaptor.findCookTalkFollows(authUserId, followIds, pageable);
    }

    public RecipeDto getRecipe(Long postId, ItCookUser authUser) {
        return postAdaptor.findRecipe(postId, authUser.getId());
    }

    public Page<MyRecipeResponse> getPostsByUserId(Long userId, Pageable pageable) {
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

    @Transactional
    public void deletePost(Long postId) {
        Post postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(PostErrorCode.POST_NOT_EXIST));
        PostFlag.checkDisablePostFlag(postEntity.getPostFlag());

        postEntity.deletePost();
    }

    public Page<HomeInterestDto> fetchFindPostsWithLikedAndArchiveDtoByCookingTheme(
            CookingType cookingTheme, Long userId, Pageable pageable) {
        return postAdaptor.findPostsWithLikedAndArchiveDtoByCookingTheme(cookingTheme, userId,
                pageable);
    }

    public Page<HomeSpecialDto> fetchFindPostsWithLikedAndArchiveDtoByLifeTypeDefaultHealthDiet(
            LifeType lifeType, Long userId, Pageable pageable) {
        return postAdaptor.findPostsWithLikedAndArchiveDtoByLifeTypeDefaultHealthDiet(lifeType,
                userId, pageable);
    }

    public Page<OtherPagePostInfoResponse> getOtherPageInfo(
            ItCookUser user, Long otherUserId, Pageable pageable
    ) {
        return postAdaptor.getOtherPagePostInfo(user.getId(), otherUserId, pageable);
    }

//    public List<Post> searchByRecipeNameOrIngredients(
//            Long lastId, List<String> names, Integer size
//    ) {
//        return postQuerydslRepository.findNamesWithPagination(lastId, names, size);
//    }


}
