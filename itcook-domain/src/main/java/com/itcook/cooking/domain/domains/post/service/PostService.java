package com.itcook.cooking.domain.domains.post.service;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.infra.s3.ImageUrlDto;
import com.itcook.cooking.domain.domains.post.domain.adaptor.PostAdaptor;
import com.itcook.cooking.domain.domains.post.domain.adaptor.RecipeProcessAdapter;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.entity.PostCookingTheme;
import com.itcook.cooking.domain.domains.post.domain.entity.PostImageRegisterService;
import com.itcook.cooking.domain.domains.post.domain.entity.RecipeProcess;
import com.itcook.cooking.domain.domains.post.domain.entity.dto.RecipeAddDto;
import com.itcook.cooking.domain.domains.post.domain.entity.validator.PostValidator;
import com.itcook.cooking.domain.domains.post.domain.enums.CookingType;
import com.itcook.cooking.domain.domains.post.domain.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.domain.repository.PostRepository;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.CookTalkFeedDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.CookTalkFollowDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.HomeInterestDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.HomeSpecialDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.RecipeDto;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.response.MyRecipeResponse;
import com.itcook.cooking.domain.domains.post.service.dto.reponse.RecipeAddResponse;
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

import static com.itcook.cooking.domain.domains.post.domain.entity.dto.RecipeAddDto.RecipeProcessAddDto;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRegisterService postImageRegisterService;
    private final PostAdaptor postAdaptor;
    private final RecipeProcessAdapter recipeProcessAdapter;
    private final PostValidator postValidator;

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

    public RecipeAddResponse addPost(RecipeAddDto recipeAddDto) {
        Long authUser = recipeAddDto.userId();

        Post post = Post.addPost(recipeAddDto, postValidator);
        List<PostCookingTheme> postCookingThemes = PostCookingTheme
                .addPostCookingTheme(recipeAddDto.cookingType(), post, postValidator);
        post.updateCookingTheme(postCookingThemes);

        Post saveRecipe = postAdaptor.savePost(post);

        ImageUrlDto mainImageUrlDto = postImageRegisterService
                .getPostImageUrlDto(authUser, saveRecipe.getId(), recipeAddDto.mainFileExtension());
        post.updateFileExtension(mainImageUrlDto.getKey(), postValidator);

        List<RecipeProcessAddDto> recipeProcessAddDtoList = recipeAddDto.recipeProcess();

        List<ImageUrlDto> subImageUrlDto = recipeProcessAddDtoList.stream()
                .map(rpDto -> postImageRegisterService
                        .getRecipeImageUrlDto(authUser, saveRecipe.getId(), rpDto.fileExtension())).toList();

        List<RecipeProcess> recipeProcesses = RecipeProcess.addRecipeProcess(recipeProcessAddDtoList, post);
        List<String> list = subImageUrlDto.stream()
                .map(ImageUrlDto::getKey)
                .toList();

        for (int i = 0; i < list.size() && i < recipeProcesses.size(); i++) {
            recipeProcesses.get(i).updateFileExtension(list.get(i));
        }
        post.addRecipeProcess(recipeProcesses);

        postAdaptor.savePost(post);
        return RecipeAddResponse.of(
                saveRecipe.getId(),
                mainImageUrlDto.getUrl(),
                subImageUrlDto.stream().map(ImageUrlDto::getUrl).toList());
    }

    public Post updatePost(Post postUpdateData, ImageUrlDto mainImageUrlDto) {
        Post postEntityData = postRepository.findById(postUpdateData.getId()).orElse(null);

        if (postEntityData == null) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }

        if (mainImageUrlDto != null) {
            postUpdateData.updateFileExtension(mainImageUrlDto.getKey(), postValidator);
        } else {
            postUpdateData.updateFileExtension(postEntityData.getPostImagePath(), postValidator);
        }

        postEntityData.updatePost(postUpdateData);

        return postUpdateData;
    }

    @Transactional
    public void removePost(Long userId, Long postId) {
        Post postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new ApiException(PostErrorCode.POST_NOT_EXIST));

        PostFlag.checkDisablePostFlag(postEntity.getPostFlag());
        if (!postEntity.isAuthor(userId)) {
            throw new ApiException(PostErrorCode.POST_NOT_PERMISSION_DELETE);
        }
        postEntity.removePost();
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
