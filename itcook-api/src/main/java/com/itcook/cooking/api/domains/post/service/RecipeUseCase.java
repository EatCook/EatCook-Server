package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.domains.post.dto.recipe.RecipeImageUrlDto;
import com.itcook.cooking.api.domains.post.dto.recipe.RecipeProcessDto;
import com.itcook.cooking.api.domains.post.dto.recipe.RecipeProcessGetResponse;
import com.itcook.cooking.api.domains.post.dto.recipe.RecipeReadDto;
import com.itcook.cooking.api.domains.post.dto.request.RecipeCreateRequest;
import com.itcook.cooking.api.domains.post.dto.request.RecipeDeleteRequest;
import com.itcook.cooking.api.domains.post.dto.request.RecipeUpdateRequest;
import com.itcook.cooking.api.domains.post.dto.response.RecipeCreateResponse;
import com.itcook.cooking.api.domains.post.dto.response.RecipeGetResponse;
import com.itcook.cooking.api.domains.post.dto.response.RecipeUpdateResponse;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.archive.domain.entity.Archive;
import com.itcook.cooking.domain.domains.infra.s3.ImageUrlDto;
import com.itcook.cooking.domain.domains.like.domain.entity.Liked;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.entity.PostCookingTheme;
import com.itcook.cooking.domain.domains.post.domain.entity.RecipeProcess;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.RecipeDto;
import com.itcook.cooking.domain.domains.post.service.PostCookingThemeService;
import com.itcook.cooking.domain.domains.post.service.PostService;
import com.itcook.cooking.domain.domains.post.service.RecipeProcessService;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RecipeUseCase {

    private final UserService userService;
    private final PostService postService;
    private final RecipeProcessService recipeProcessService;
    private final PostCookingThemeService postCookingThemeService;
    private final PostValidationUseCase postValidationUseCase;

    //create
    @Transactional
    public RecipeCreateResponse createRecipe(
            String username,
            RecipeCreateRequest recipeCreateRequest) {
        //유저 검증
        ItCookUser itCookUser = userService.findUserByEmail(username);
        //Post, RecipeProcess, PostCookingTheme 저장
        Post post = recipeCreateRequest.toPostDomain(itCookUser.getId());
        Post savePostData = postService.createPost(post);

        List<RecipeProcess> recipeProcesses = recipeCreateRequest.toRecipeProcessDomain(
                savePostData);
        List<RecipeProcess> saveRecipeProcess = recipeProcessService.createRecipeProcess(
                recipeProcesses);

        List<PostCookingTheme> postCookingTheme = recipeCreateRequest.toPostCookingTheme(post);
        postCookingThemeService.createPostCookingTheme(postCookingTheme);

        RecipeImageUrlDto recipeImageUrlDto = getRecipeImageUrls(
                recipeCreateRequest.getMainFileExtension(), recipeCreateRequest.getRecipeProcess(),
                itCookUser, savePostData, saveRecipeProcess);

        //Post, RecipeProcess 이미지 경로 업데이트
        updateRecipeImagePath(savePostData, recipeImageUrlDto, saveRecipeProcess);

        return RecipeCreateResponse.builder()
                .postId(post.getId())
                .mainPresignedUrl(recipeImageUrlDto.getMainImageUrl().getUrl())
                .recipeProcessPresignedUrl(recipeImageUrlDto.getRecipeProcessImageUrl().stream()
                        .map(ImageUrlDto::getUrl).toList())
                .build();
    }

    private RecipeImageUrlDto getRecipeImageUrls(String mainImageFileExtension,
                                                 List<RecipeProcessDto> recipeProcessDtos, ItCookUser itCookUser, Post savePostData,
                                                 List<RecipeProcess> saveRecipeProcess1) {
        ImageUrlDto mainUrl = getMainImagePresignedUrl(mainImageFileExtension, itCookUser,
                savePostData);
        List<ImageUrlDto> recipeProcessUrl = getRecipeProcessPresignedUrl(recipeProcessDtos,
                itCookUser, savePostData);

        return RecipeImageUrlDto.builder()
                .mainImageUrl(mainUrl)
                .recipeProcessImageUrl(recipeProcessUrl)
                .build();
    }

    private ImageUrlDto getMainImagePresignedUrl(String mainFileExtension, ItCookUser itCookUser,
                                                 Post savePostData) {
        return postValidationUseCase.getPostFileExtensionValidation(itCookUser.getId(),
                savePostData.getId(), mainFileExtension);
    }

    private List<ImageUrlDto> getRecipeProcessPresignedUrl(List<RecipeProcessDto> recipeProcessDtos,
                                                           ItCookUser itCookUser, Post savePostData) {
        return recipeProcessDtos
                .parallelStream()
                .map(recipeProcess -> postValidationUseCase.getRecipeProcessFileExtensionValidation(
                        itCookUser.getId(), savePostData.getId(), recipeProcess))
                .toList();
    }

    private static void updateRecipeImagePath(Post savePostData,
                                              RecipeImageUrlDto recipeImageUrlDto, List<RecipeProcess> saveRecipeProcess) {
        savePostData.updateFileExtension(recipeImageUrlDto.getMainImageUrl().getKey());
        IntStream.range(0, saveRecipeProcess.size())
                .forEach(i -> saveRecipeProcess.get(i).updateFileExtension(
                        recipeImageUrlDto.getRecipeProcessImageUrl().get(i).getKey()));
    }

    public RecipeGetResponse getReadRecipe(String email, Long postId) {
        ItCookUser authUser = userService.findUserByEmail(email);
        
        RecipeDto recipe = postService.getRecipe(postId, authUser);
        return RecipeGetResponse.of(recipe, authUser.getId());
    }

    private static void collectUniquePostCookingThemeAndRecipeProcess(
            List<Object[]> recipeReadDomainDto,
            Set<PostCookingTheme> uniquePostCookingTheme,
            Set<RecipeProcess> uniqueRecipeProcess,
            Set<Liked> uniqueLiked,
            Set<Archive> uniqueArchive
    ) {

        recipeReadDomainDto.forEach(objects -> {
            uniquePostCookingTheme.add((PostCookingTheme) objects[2]);
            uniqueRecipeProcess.add((RecipeProcess) objects[3]);
            if (objects[4] != null) {
                uniqueLiked.add((Liked) objects[4]);
            }
            if (objects[5] != null) {
                uniqueArchive.add((Archive) objects[5]);
            }
        });
    }

    private static List<String> extractCookingTypeNames(
            Set<PostCookingTheme> uniquePostCookingTheme) {
        return uniquePostCookingTheme.stream()
                .map(postCookingTheme -> postCookingTheme.getCookingType().getCookingTypeName())
                .toList();
    }


    private static List<RecipeProcessGetResponse> extractRecipeProcess(
            Set<RecipeProcess> uniqueRecipeProcess) {
        return uniqueRecipeProcess.stream()
                .map(recipeProcess -> RecipeProcessGetResponse.builder()
                        .stepNum(recipeProcess.getStepNum())
                        .recipeWriting(recipeProcess.getRecipeWriting())
                        .recipeProcessImagePath(recipeProcess.getRecipeProcessImagePath())
                        .build())
                .toList();
    }

    private boolean isFollowingCheck(ItCookUser findByUserEmail, Post post) {
        List<Long> follow = findByUserEmail.getFollow();
        return postValidationUseCase.getFollowingCheck(post.getUserId(), follow);
    }

    private static RecipeReadDto getRecipeReadDto(Post post, ItCookUser itCookUser,
                                                  List<String> postCookingThemeList,
                                                  List<RecipeProcessGetResponse> recipeProcessDtoList,
                                                  boolean followingCheck, List<Liked> findAllLiked, boolean likedValidation,
                                                  boolean archiveValidation
    ) {
        return RecipeReadDto.builder()
                .postId(post.getId())
                .recipeName(post.getRecipeName())
                .recipeTime(post.getRecipeTime())
                .introduction(post.getIntroduction())
                .postImagePath(post.getPostImagePath())
                .createdAt(post.getCreatedAt())
                .lastModifiedAt(post.getLastModifiedAt())
                .userId(itCookUser.getId())
                .nickName(itCookUser.getNickName())
                .profile(itCookUser.getProfile())
                .followerCount(itCookUser.getFollow().size())
                .foodIngredientsCnt(post.getFoodIngredients().size())
                .foodIngredients(post.getFoodIngredients())
                .cookingType(postCookingThemeList)
                .recipeProcess(recipeProcessDtoList)
                .likedCount(findAllLiked.size())
                .likedCheck(likedValidation)
                .followCheck(followingCheck)
                .archiveCheck(archiveValidation)
                .build();

    }

    //update
    @Transactional
    public RecipeUpdateResponse updateRecipe(RecipeUpdateRequest recipeUpdateRequest) {
        ImageUrlDto mainImageUrlDto = null;
        if (getUpdateFileExtensionValidation(recipeUpdateRequest.getMainFileExtension())) {
            mainImageUrlDto = postValidationUseCase.getPostFileExtensionValidation(
                    recipeUpdateRequest.getUserId(), recipeUpdateRequest.getPostId(),
                    recipeUpdateRequest.getMainFileExtension());
        }

        Post postUpdateData = recipeUpdateRequest.toPostDomain();
        Post postEntityData = postService.updatePost(postUpdateData, mainImageUrlDto);

        List<RecipeProcess> recipeProcessesData = recipeUpdateRequest.toRecipeProcessDomain(
                postEntityData);

        List<ImageUrlDto> recipeProcessImageUrlDto = updateRecipeProcessFileExtensionsValidation(
                recipeUpdateRequest, recipeProcessesData);
        recipeProcessService.updateRecipeProcess(recipeProcessesData, postEntityData);

        List<PostCookingTheme> postCookingThemeData = recipeUpdateRequest.toPostCookingThemeDomain(
                postEntityData);
        postCookingThemeService.updatePostCookingTheme(postCookingThemeData, postEntityData);

        String mainPresignedUrl = mainImageNullCheckValidation(mainImageUrlDto);

        List<String> recipeProcessPresignedUrls = recipeProcessImageNullCheckValiation(
                recipeProcessImageUrlDto);

        return getRecipeUpdateResponse(mainPresignedUrl, recipeProcessPresignedUrls);
    }

    private List<ImageUrlDto> updateRecipeProcessFileExtensionsValidation(
            RecipeUpdateRequest recipeUpdateRequest, List<RecipeProcess> recipeProcessesData) {
        return recipeUpdateRequest.getRecipeProcess().stream()
                .filter(recipeProcess -> getUpdateFileExtensionValidation(
                        recipeProcess.getFileExtension()))
                .map(recipeProcess -> {
                    ImageUrlDto recipeProcessFileImageUrlDto = postValidationUseCase.getRecipeProcessFileExtensionValidation(
                            recipeUpdateRequest.getUserId(), recipeUpdateRequest.getPostId(),
                            recipeProcess);
                    recipeProcessesData.stream()
                            .filter(recipeProcessesDatum -> recipeProcessesDatum.getStepNum()
                                    == recipeProcess.getStepNum())
                            .forEach(
                                    recipeProcessesDatum -> recipeProcessesDatum.updateFileExtension(
                                            recipeProcessFileImageUrlDto.getKey()));
                    return recipeProcessFileImageUrlDto;
                })
                .toList();
    }

    private static List<String> recipeProcessImageNullCheckValiation(
            List<ImageUrlDto> recipeProcessImageUrlDto) {
        return recipeProcessImageUrlDto.stream()
                .map(ImageUrlDto::getUrl)
                .toList();
    }


    private static RecipeUpdateResponse getRecipeUpdateResponse(String mainPresignedUrl,
                                                                List<String> recipeProcessPresignedUrls) {
        return RecipeUpdateResponse.builder()
                .mainPresignedUrl(mainPresignedUrl)
                .recipeProcessPresignedUrl(recipeProcessPresignedUrls)
                .build();
    }

    private static String mainImageNullCheckValidation(ImageUrlDto mainImageUrlDto) {
        return Optional.ofNullable(mainImageUrlDto)
                .map(ImageUrlDto::getUrl)
                .orElse(null);
    }

    private Boolean getUpdateFileExtensionValidation(String fileExtension) {
        return !fileExtension.equals("default");
    }

    @Transactional
    public void deleteRecipe(
            String username,
            RecipeDeleteRequest recipeDeleteRequest
    ) {
        userService.findUserByEmail(username);

        postService.deletePost(recipeDeleteRequest.getPostId());
    }

}
