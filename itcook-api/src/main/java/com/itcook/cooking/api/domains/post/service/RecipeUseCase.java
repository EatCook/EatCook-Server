package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.domains.post.dto.request.RecipeUpdateRequest;
import com.itcook.cooking.api.domains.post.dto.response.RecipeGetResponse;
import com.itcook.cooking.api.domains.post.dto.response.RecipeUpdateResponse;
import com.itcook.cooking.api.domains.post.service.dto.RecipeAddServiceDto;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.infra.s3.ImageUrlDto;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.entity.PostCookingTheme;
import com.itcook.cooking.domain.domains.post.domain.entity.RecipeProcess;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.RecipeDto;
import com.itcook.cooking.domain.domains.post.service.PostCookingThemeService;
import com.itcook.cooking.domain.domains.post.service.PostService;
import com.itcook.cooking.domain.domains.post.service.RecipeProcessService;
import com.itcook.cooking.domain.domains.post.service.dto.reponse.RecipeAddResponse;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    /**
     * 레시피 생성
     */
    @Transactional
    public RecipeAddResponse addRecipe(
            RecipeAddServiceDto recipeAddServiceDto
    ) {
        Long authUserId = userService.findIdByEmail(recipeAddServiceDto.email());

        return postService.addPost(recipeAddServiceDto.toDomainDto(authUserId));
    }

    /**
     * 레시피 상세 조회
     */
    public RecipeGetResponse getReadRecipe(String email, Long postId) {
        ItCookUser authUser = userService.findUserByEmail(email);

        RecipeDto recipe = postService.getRecipe(postId, authUser);
        List<Long> follow = authUser.getFollow();

        boolean contains = follow.contains(recipe.getItCookUser().getId());
        return RecipeGetResponse.of(recipe, contains);
    }

    //update
    @Transactional
    public RecipeUpdateResponse updateRecipe(RecipeUpdateRequest recipeUpdateRequest, Long recipeId) {
        ImageUrlDto mainImageUrlDto = null;
        if (getUpdateFileExtensionValidation(recipeUpdateRequest.getMainFileExtension())) {
            mainImageUrlDto = postValidationUseCase.getPostFileExtensionValidation(
                    recipeUpdateRequest.getUserId(), recipeId,
                    recipeUpdateRequest.getMainFileExtension());
        }

        Post postUpdateData = recipeUpdateRequest.toPostDomain();
        Post postEntityData = postService.updatePost(postUpdateData, mainImageUrlDto);

        List<RecipeProcess> recipeProcessesData = recipeUpdateRequest.toRecipeProcessDomain(
                postEntityData);

        List<ImageUrlDto> recipeProcessImageUrlDto = updateRecipeProcessFileExtensionsValidation(
                recipeUpdateRequest, recipeProcessesData, recipeId);
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
            RecipeUpdateRequest recipeUpdateRequest, List<RecipeProcess> recipeProcessesData, Long recipeId) {
        return recipeUpdateRequest.getRecipeProcess().stream()
                .filter(recipeProcess -> getUpdateFileExtensionValidation(
                        recipeProcess.getFileExtension()))
                .map(recipeProcess -> {
                    ImageUrlDto recipeProcessFileImageUrlDto = postValidationUseCase.getRecipeProcessFileExtensionValidation(
                            recipeUpdateRequest.getUserId(), recipeId,
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

    /**
     * 레시피 삭제
     */
    @Transactional
    public void removeRecipe(String email, Long recipeId) {
        Long authUserId = userService.findIdByEmail(email);

        postService.removePost(authUserId, recipeId);
    }

}
