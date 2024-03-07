package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.domains.post.dto.RecipeDto;
import com.itcook.cooking.api.domains.post.dto.request.RecipeCreateRequest;
import com.itcook.cooking.api.domains.post.dto.request.RecipeDeleteRequest;
import com.itcook.cooking.api.domains.post.dto.request.RecipeReadRequest;
import com.itcook.cooking.api.domains.post.dto.request.RecipeUpdateRequest;
import com.itcook.cooking.api.domains.post.dto.response.RecipeResponse;
import com.itcook.cooking.api.global.annotation.UseCase;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.entity.PostCookingTheme;
import com.itcook.cooking.domain.domains.post.entity.RecipeProcess;
import com.itcook.cooking.domain.domains.post.service.PostCookingThemeDomainService;
import com.itcook.cooking.domain.domains.post.service.PostDomainService;
import com.itcook.cooking.domain.domains.post.service.RecipeProcessDomainService;
import com.itcook.cooking.domain.domains.user.entity.Archive;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.ArchiveDomainService;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RecipeUseCase {

    private final UserDomainService userDomainService;
    private final PostDomainService postDomainService;
    private final RecipeProcessDomainService recipeProcessDomainService;
    private final PostCookingThemeDomainService postCookingThemeDomainService;
    private final ArchiveDomainService archiveDomainService;
    private final PostValidationUseCase postValidationUseCase;

    @Transactional
    public void createRecipe(RecipeCreateRequest recipeCreateRequest) {
        //유저 검증
        ItCookUser itCookUser = userDomainService.fetchFindByEmail(recipeCreateRequest.getEmail());

        Post post = recipeCreateRequest.toPostDomain(itCookUser.getId());
        Post savePostData = postDomainService.createPost(post);

        List<RecipeProcess> recipeProcess = recipeCreateRequest.toRecipeProcessDomain(savePostData);
        recipeProcessDomainService.createRecipeProcess(recipeProcess);

        List<PostCookingTheme> postCookingTheme = recipeCreateRequest.toPostCookingTheme(post);
        postCookingThemeDomainService.createPostCookingTheme(postCookingTheme);

    }

    public RecipeResponse getReadRecipe(RecipeReadRequest recipeReadRequest) {
        List<RecipeDto> recipeDtos;
        Optional<Post> postData;
        ItCookUser findByUserEmail = userDomainService.fetchFindByEmail(recipeReadRequest.getEmail());

        postData = postDomainService.fetchFindPost(recipeReadRequest.getPostId());

        recipeDtos = postValidationUseCase.postUserMatchingValidation(postData.stream().toList(), RecipeDto::new);

        Set<Long> followingSet = new HashSet<>(findByUserEmail.getFollow());
        recipeDtos.forEach(recipeDto -> postValidationUseCase.getFollowingCheck(recipeDto, followingSet));

        List<RecipeProcess> findRecipeProcesses = recipeProcessDomainService.readRecipeProcess(postData.get());
        List<PostCookingTheme> findAllPostCookingTheme = postCookingThemeDomainService.findAllPostCookingTheme(postData.get());

        List<ItCookUser> likedItCookUser = userDomainService.findLiked(postData.get().getId());
        boolean likedValidation = postValidationUseCase.getLikedValidation(findByUserEmail.getLikeds(), postData.get().getId());

        //보관함 조회
        List<Archive> findByArchiveToItCookUser = archiveDomainService.getFindByItCookUserId(findByUserEmail.getId());
        boolean archiveValidation = postValidationUseCase.getArchiveValidation(findByArchiveToItCookUser, postData.get().getId());

        recipeDtos.get(0).toRecipeDto(findRecipeProcesses, findAllPostCookingTheme, likedItCookUser, likedValidation, archiveValidation);

        RecipeDto recipeDto = recipeDtos.get(0);

        return recipeDtos.stream()
                .map(RecipeResponse::of)
                .collect(Collectors.toList()).get(0);


    }

    @Transactional
    public void updateRecipe(RecipeUpdateRequest recipeUpdateRequest) {
        Post postUpdateData = recipeUpdateRequest.toPostDomain();
        Post postEntityData = postDomainService.updatePost(postUpdateData);

        List<RecipeProcess> recipeProcessesData = recipeUpdateRequest.toRecipeProcessDomain(postEntityData);
        recipeProcessDomainService.updateRecipeProcess(recipeProcessesData, postEntityData);
    }

    @Transactional
    public void deleteRecipe(RecipeDeleteRequest recipeDeleteRequest) {
        userDomainService.fetchFindByEmail(recipeDeleteRequest.getEmail());

        postDomainService.deletePost(recipeDeleteRequest.getPostId());
    }

}
