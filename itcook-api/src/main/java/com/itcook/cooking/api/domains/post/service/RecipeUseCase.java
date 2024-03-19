package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.domains.post.dto.recipe.RecipeImageUrlDto;
import com.itcook.cooking.api.domains.post.dto.recipe.RecipeProcessDto;
import com.itcook.cooking.api.domains.post.dto.recipe.RecipeProcessReadDto;
import com.itcook.cooking.api.domains.post.dto.recipe.RecipeReadDto;
import com.itcook.cooking.api.domains.post.dto.request.RecipeCreateRequest;
import com.itcook.cooking.api.domains.post.dto.request.RecipeDeleteRequest;
import com.itcook.cooking.api.domains.post.dto.request.RecipeReadRequest;
import com.itcook.cooking.api.domains.post.dto.request.RecipeUpdateRequest;
import com.itcook.cooking.api.domains.post.dto.response.RecipeCreateResponse;
import com.itcook.cooking.api.domains.post.dto.response.RecipeReadResponse;
import com.itcook.cooking.api.domains.post.dto.response.RecipeUpdateResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.entity.PostCookingTheme;
import com.itcook.cooking.domain.domains.post.entity.RecipeProcess;
import com.itcook.cooking.domain.domains.post.service.LikedDomainService;
import com.itcook.cooking.domain.domains.post.service.PostCookingThemeDomainService;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.domain.domains.post.service.RecipeProcessDomainService;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.ArchiveDomainService;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import com.itcook.cooking.infra.s3.ImageUrlDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.IntStream;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RecipeUseCase {

    private final UserDomainService userDomainService;
    private final PostDomainService postDomainService;
    private final RecipeProcessDomainService recipeProcessDomainService;
    private final PostCookingThemeDomainService postCookingThemeDomainService;
    private final LikedDomainService likedDomainService;
    private final ArchiveDomainService archiveDomainService;

    private final PostValidationUseCase postValidationUseCase;

    //create
    @Transactional
    public RecipeCreateResponse createRecipe(RecipeCreateRequest recipeCreateRequest) {
        //유저 검증
        ItCookUser itCookUser = userDomainService.fetchFindByEmail(recipeCreateRequest.getEmail());
        //Post, RecipeProcess, PostCookingTheme 저장
        Post post = recipeCreateRequest.toPostDomain(itCookUser.getId());
        Post savePostData = postDomainService.createPost(post);

        List<RecipeProcess> recipeProcesses = recipeCreateRequest.toRecipeProcessDomain(savePostData);
        List<RecipeProcess> saveRecipeProcess = recipeProcessDomainService.createRecipeProcess(recipeProcesses);

        List<PostCookingTheme> postCookingTheme = recipeCreateRequest.toPostCookingTheme(post);
        postCookingThemeDomainService.createPostCookingTheme(postCookingTheme);

        RecipeImageUrlDto recipeImageUrlDto = getRecipeImageUrls(recipeCreateRequest.getMainFileExtension(), recipeCreateRequest.getRecipeProcess(), itCookUser, savePostData, saveRecipeProcess);

        //Post, RecipeProcess 이미지 경로 업데이트
        updateRecipeImagePath(savePostData, recipeImageUrlDto, saveRecipeProcess);

        return RecipeCreateResponse.builder()
                .mainPresignedUrl(recipeImageUrlDto.getMainImageUrl().getUrl())
                .recipeProcessPresignedUrl(recipeImageUrlDto.getRecipeProcessImageUrl().stream().map(ImageUrlDto::getUrl).toList())
                .build();
    }

    private RecipeImageUrlDto getRecipeImageUrls(String mainImageFileExtension, List<RecipeProcessDto> recipeProcessDtos, ItCookUser itCookUser, Post savePostData, List<RecipeProcess> saveRecipeProcess1) {
        ImageUrlDto mainUrl = getMainImagePresignedUrl(mainImageFileExtension, itCookUser, savePostData);
        List<ImageUrlDto> recipeProcessUrl = getRecipeProcessPresignedUrl(recipeProcessDtos, itCookUser, savePostData);

        return RecipeImageUrlDto.builder()
                .mainImageUrl(mainUrl)
                .recipeProcessImageUrl(recipeProcessUrl)
                .build();
    }

    private ImageUrlDto getMainImagePresignedUrl(String mainFileExtension, ItCookUser itCookUser, Post savePostData) {
        return postValidationUseCase.getPostFileExtensionValidation(itCookUser.getId(), savePostData.getId(), mainFileExtension);
    }

    private List<ImageUrlDto> getRecipeProcessPresignedUrl(List<RecipeProcessDto> recipeProcessDtos, ItCookUser itCookUser, Post savePostData) {
        return recipeProcessDtos
                .parallelStream()
                .map(recipeProcess -> postValidationUseCase.getRecipeProcessFileExtensionValidation(itCookUser.getId(), savePostData.getId(), recipeProcess))
                .toList();
    }

    private static void updateRecipeImagePath(Post savePostData, RecipeImageUrlDto recipeImageUrlDto, List<RecipeProcess> saveRecipeProcess) {
        savePostData.updateFileExtension(recipeImageUrlDto.getMainImageUrl().getKey());
        IntStream.range(0, saveRecipeProcess.size())
                .forEach(i -> saveRecipeProcess.get(i).updateFileExtension(recipeImageUrlDto.getRecipeProcessImageUrl().get(i).getKey()));
    }

    //read
    public RecipeReadResponse getReadRecipe(RecipeReadRequest recipeReadRequest) {
        ItCookUser findByUserEmail = userDomainService.fetchFindByEmail(recipeReadRequest.getEmail());
        Optional<Post> postData = postDomainService.fetchFindPost(recipeReadRequest.getPostId());

        List<RecipeReadDto> recipeDtos = postValidationUseCase.postUserMatchingValidation(
                postData.stream().toList(), RecipeReadDto::new);

        Set<Long> followingSet = new HashSet<>(findByUserEmail.getFollow());
        recipeDtos.forEach(recipeDto -> postValidationUseCase.getFollowingCheck(recipeDto, followingSet));

        List<RecipeProcessReadDto> recipeProcessDtos = recipeProcessDomainService.readRecipeProcess(postData.get())
                .stream()
                .map(findRecipeProcess -> RecipeProcessReadDto.builder()
                        .stepNum(findRecipeProcess.getStepNum())
                        .recipeWriting(findRecipeProcess.getRecipeWriting())
                        .recipeProcessImagePath(findRecipeProcess.getRecipeProcessImagePath())
                        .build())
                .toList();

        List<PostCookingTheme> findAllPostCookingTheme = postCookingThemeDomainService.findAllPostCookingTheme(postData.get());

        List<Liked> findAllLiked = likedDomainService.getFindLiked(postData.get().getId());

        boolean likedValidation = postValidationUseCase.getLikedValidation(findAllLiked, findByUserEmail.getId());

        boolean archiveValidation = postValidationUseCase.getArchiveValidation(archiveDomainService.getFindByItCookUserId(findByUserEmail.getId()), postData.get().getId());

        recipeDtos.get(0).toRecipeDto(recipeProcessDtos, findAllPostCookingTheme, findAllLiked, likedValidation, archiveValidation);

        return RecipeReadResponse.of(recipeDtos.get(0));
    }

    @Transactional
    public RecipeUpdateResponse updateRecipe(RecipeUpdateRequest recipeUpdateRequest) {
        ImageUrlDto mainImageUrlDto = null;
        if (getUpdateFileExtensionValidation(recipeUpdateRequest.getMainFileExtension())) {
            mainImageUrlDto = postValidationUseCase.getPostFileExtensionValidation(recipeUpdateRequest.getUserId(), recipeUpdateRequest.getPostId(), recipeUpdateRequest.getMainFileExtension());
        }

        Post postUpdateData = recipeUpdateRequest.toPostDomain();
        Post postEntityData = postDomainService.updatePost(postUpdateData, mainImageUrlDto);

        List<RecipeProcess> recipeProcessesData = recipeUpdateRequest.toRecipeProcessDomain(postEntityData);

        List<ImageUrlDto> recipeProcessImageUrlDto = updateRecipeProcessFileExtensionsValidation(recipeUpdateRequest, recipeProcessesData);
        recipeProcessDomainService.updateRecipeProcess(recipeProcessesData, postEntityData);

        List<PostCookingTheme> postCookingThemeData = recipeUpdateRequest.toPostCookingThemeDomain(postEntityData);
        postCookingThemeDomainService.updatePostCookingTheme(postCookingThemeData, postEntityData);

        String mainPresignedUrl = mainImageNullCheckValidation(mainImageUrlDto);

        List<String> recipeProcessPresignedUrls = recipeProcessImageNullCheckValiation(recipeProcessImageUrlDto);

        return getRecipeUpdateResponse(mainPresignedUrl, recipeProcessPresignedUrls);
    }

    private static RecipeUpdateResponse getRecipeUpdateResponse(String mainPresignedUrl, List<String> recipeProcessPresignedUrls) {
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

    private static List<String> recipeProcessImageNullCheckValiation(List<ImageUrlDto> recipeProcessImageUrlDto) {
        return recipeProcessImageUrlDto.stream()
                .map(ImageUrlDto::getUrl)
                .toList();
    }

    private List<ImageUrlDto> updateRecipeProcessFileExtensionsValidation(RecipeUpdateRequest recipeUpdateRequest, List<RecipeProcess> recipeProcessesData) {
        return recipeUpdateRequest.getRecipeProcess().stream()
                .filter(recipeProcess -> getUpdateFileExtensionValidation(recipeProcess.getFileExtension()))
                .map(recipeProcess -> {
                    ImageUrlDto recipeProcessFileImageUrlDto = postValidationUseCase.getRecipeProcessFileExtensionValidation(recipeUpdateRequest.getUserId(), recipeUpdateRequest.getPostId(), recipeProcess);
                    recipeProcessesData.stream()
                            .filter(recipeProcessesDatum -> recipeProcessesDatum.getStepNum() == recipeProcess.getStepNum())
                            .forEach(recipeProcessesDatum -> recipeProcessesDatum.updateFileExtension(recipeProcessFileImageUrlDto.getKey()));
                    return recipeProcessFileImageUrlDto;
                })
                .toList();
    }

    private Boolean getUpdateFileExtensionValidation(String fileExtension) {
        return !fileExtension.equals("default");
    }

    @Transactional
    public void deleteRecipe(RecipeDeleteRequest recipeDeleteRequest) {
        userDomainService.fetchFindByEmail(recipeDeleteRequest.getEmail());

        postDomainService.deletePost(recipeDeleteRequest.getPostId());
    }

}
