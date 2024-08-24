package com.itcook.cooking.domain.domains.post.service;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.errorcode.RecipeProcessErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.infra.s3.ImageFileExtension;
import com.itcook.cooking.domain.domains.infra.s3.ImageUrlDto;
import com.itcook.cooking.domain.domains.post.domain.adaptor.PostAdaptor;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.entity.PostCookingTheme;
import com.itcook.cooking.domain.domains.post.domain.entity.PostImageRegisterService;
import com.itcook.cooking.domain.domains.post.domain.entity.RecipeProcess;
import com.itcook.cooking.domain.domains.post.domain.entity.dto.RecipeAddDto;
import com.itcook.cooking.domain.domains.post.domain.entity.dto.RecipeUpdateDto;
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
import com.itcook.cooking.domain.domains.post.service.dto.reponse.RecipeUpdateResponse;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import com.itcook.cooking.domain.domains.user.service.dto.response.OtherPagePostInfoResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.itcook.cooking.domain.domains.post.domain.entity.dto.RecipeAddDto.RecipeProcessAddDto;
import static com.itcook.cooking.domain.domains.post.domain.entity.dto.RecipeUpdateDto.RecipeProcessUpdateDto;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRegisterService postImageRegisterService;
    private final PostAdaptor postAdaptor;
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

    @Transactional
    public RecipeAddResponse addPost(RecipeAddDto recipeAddDto) {
        Long authUser = recipeAddDto.userId();

        Post post = Post.addPost(recipeAddDto, postValidator);
        List<PostCookingTheme> postCookingThemes = PostCookingTheme
                .addPostCookingTheme(recipeAddDto.cookingType(), post, postValidator);
        post.updateCookingTheme(postCookingThemes);

        Post saveRecipe = postAdaptor.savePost(post);

        ImageUrlDto mainImageUrlDto = postImageRegisterService
                .getPostImageUrlDto(authUser, saveRecipe.getId(), recipeAddDto.mainFileExtension());
        post.updatePostImagePath(mainImageUrlDto.getKey(), postValidator);

        List<RecipeProcessAddDto> recipeProcessAddDtoList = recipeAddDto.recipeProcess();

        List<ImageUrlDto> subImageUrlDto = recipeProcessAddDtoList.stream()
                .map(rpDto -> postImageRegisterService
                        .getRecipeImageUrlDto(authUser, saveRecipe.getId(), rpDto.fileExtension())).toList();

        List<RecipeProcess> recipeProcesses = recipeProcessAddDtoList.stream()
                .map(rp -> RecipeProcess.addRecipeProcess(rp.stepNum(), rp.recipeWriting(), post)).toList();

        List<String> list = subImageUrlDto.stream()
                .map(ImageUrlDto::getKey)
                .toList();

        for (int i = 0; i < list.size() && i < recipeProcesses.size(); i++) {
            recipeProcesses.get(i).updateFileExtension(list.get(i));
        }
        post.addRecipeProcess(recipeProcesses);

        return RecipeAddResponse.of(
                saveRecipe.getId(),
                mainImageUrlDto.getUrl(),
                subImageUrlDto.stream().map(ImageUrlDto::getUrl).toList());
    }

    /**
     * 레시피 수정
     */
    public RecipeUpdateResponse updatePost(RecipeUpdateDto recipeUpdateDto) {
        Post findPost = validateUserPermission(recipeUpdateDto);
        Optional<ImageUrlDto> postImageUrlDtoOpt = updateMainImageIfNecessary(findPost, recipeUpdateDto.getMainFileExtension());

        List<RecipeProcessUpdateDto> newRecipeProcesses = recipeUpdateDto.getRecipeProcess();
        List<RecipeProcess> oldRecipeProcesses = findPost.getRecipeProcesses();

        validateStepNumOrder(newRecipeProcesses);
        //기존의 이미지 경로
        Set<String> unsupportedExtensions = validateRecipeProcessImages(newRecipeProcesses, findPost);

        Map<Integer, String> imageUrlDtoList = new HashMap<>();
        for (RecipeProcessUpdateDto newRecipeProcess : newRecipeProcesses) {
            String fileExtension = newRecipeProcess.getFileExtension();
            if (!unsupportedExtensions.contains(fileExtension)) {
                ImageUrlDto recipeImageUrlDto = postImageRegisterService.getRecipeImageUrlDto(findPost.getUserId(), findPost.getId(), fileExtension);
                newRecipeProcess.updateRecipeProcessFileExtension(recipeImageUrlDto.getKey());
                imageUrlDtoList.put(newRecipeProcess.getStepNum(), recipeImageUrlDto.getUrl());
            }
        }

        Map<Integer, RecipeProcess> oldRecipeProcessMap = oldRecipeProcesses.stream()
                .collect(Collectors.toMap(RecipeProcess::getStepNum, Function.identity()));

        for (RecipeProcessUpdateDto newProcess : newRecipeProcesses) {
            RecipeProcess oldProcess = oldRecipeProcessMap.remove(newProcess.getStepNum());
            if (oldProcess != null) {
                oldProcess.updateRecipeProcess(newProcess.getStepNum(), newProcess.getRecipeWriting(), newProcess.getFileExtension());
            } else {
                findPost.getRecipeProcesses().add(RecipeProcess.addRecipeProcessFromUpdate(
                        newProcess.getStepNum(),
                        newProcess.getRecipeWriting(),
                        newProcess.getFileExtension(),
                        findPost));
            }
        }

        for (RecipeProcess removedProcess : oldRecipeProcessMap.values()) {
            findPost.getRecipeProcesses().remove(removedProcess);
        }

        String mainImagePath = "";
        if (postImageUrlDtoOpt.isPresent()) {
            ImageUrlDto postImageUrlDto = postImageUrlDtoOpt.get();
            findPost.updatePostImagePath(postImageUrlDto.getKey(), postValidator);
            mainImagePath = postImageUrlDto.getUrl();
        }

        return RecipeUpdateResponse.of(
                findPost.getId(),
                mainImagePath,
                new ArrayList<>(imageUrlDtoList.values()));
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

    /**
     * 유저 권한 검증
     */
    private Post validateUserPermission(RecipeUpdateDto recipeUpdateDto) {
        Post findPost = postAdaptor.findByIdOrElseThrow(recipeUpdateDto.getRecipeId());
        if (!Objects.equals(findPost.getUserId(), recipeUpdateDto.getUserId())) {
            throw new ApiException(PostErrorCode.POST_NOT_PERMISSION_UPDATE);
        }
        return findPost;
    }

    /**
     * 메인 이미지의 변경이 필요 검증, 필요한 경우 업데이트하는 로직을 분리.
     */
    private Optional<ImageUrlDto> updateMainImageIfNecessary(Post findPost, String newFileExtension) {
        if (!findPost.getPostImagePath().equals(newFileExtension)) {
            return Optional.of(postImageRegisterService.getPostImageUrlDto(
                    findPost.getUserId(),
                    findPost.getId(),
                    newFileExtension));
        }
        return Optional.empty();
    }

    /**
     * 조리 과정 stepNum 순서 검증
     */
    private void validateStepNumOrder(List<RecipeProcessUpdateDto> recipeProcessUpdateDtoList) {
        IntStream.range(0, recipeProcessUpdateDtoList.size())
                .forEach(i -> {
                    if (recipeProcessUpdateDtoList.get(i).getStepNum() != i + 1) {
                        throw new ApiException(RecipeProcessErrorCode.INVALID_STEPNUM_ORDER);
                    }
                });
    }

    /**
     * 레시피 과정의 이미지 파일 확장자를 검증
     */
    private Set<String> validateRecipeProcessImages(List<RecipeProcessUpdateDto> recipeProcessUpdateDtoList, Post findPost) {
        Set<String> fileExtensionsList = recipeProcessUpdateDtoList.stream()
                .map(RecipeProcessUpdateDto::getFileExtension)
                .collect(Collectors.toSet());

        Set<String> unsupportedExtensions = ImageFileExtension.findUnsupportedExtensions(fileExtensionsList);

        Set<String> recipeProcessImagePathSet = findPost.getRecipeProcesses().stream()
                .map(RecipeProcess::getRecipeProcessImagePath)
                .collect(Collectors.toSet());

        validateImagePaths(unsupportedExtensions, recipeProcessImagePathSet);

        return unsupportedExtensions;
    }

    /**
     * 기존에 등록된 이미지 Path 여부 검증
     */
    private void validateImagePaths(Set<String> unsupportedExtensions, Set<String> recipeProcessImagePathSet) {
        Set<String> invalidExtensions = unsupportedExtensions.stream()
                .filter(extension -> !recipeProcessImagePathSet.contains(extension))
                .collect(Collectors.toSet());

        if (!invalidExtensions.isEmpty()) {
            throw new ApiException(RecipeProcessErrorCode.INVALID_IMAGE_EXTENSION);
        }
    }


//    public List<Post> searchByRecipeNameOrIngredients(
//            Long lastId, List<String> names, Integer size
//    ) {
//        return postQuerydslRepository.findNamesWithPagination(lastId, names, size);
//    }


}
