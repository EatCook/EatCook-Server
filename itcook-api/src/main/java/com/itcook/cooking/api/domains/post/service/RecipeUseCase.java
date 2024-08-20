package com.itcook.cooking.api.domains.post.service;

import com.itcook.cooking.api.domains.post.dto.response.RecipeGetResponse;
import com.itcook.cooking.api.domains.post.dto.response.RecipeUpdateResponse;
import com.itcook.cooking.api.domains.post.service.dto.RecipeAddServiceDto;
import com.itcook.cooking.api.domains.post.service.dto.RecipeUpdateServiceDto;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.RecipeDto;
import com.itcook.cooking.domain.domains.post.service.PostService;
import com.itcook.cooking.domain.domains.post.service.dto.reponse.RecipeAddResponse;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RecipeUseCase {

    private final UserService userService;
    private final PostService postService;

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

    /**
     * 레시피 수정
     */
    @Transactional
    public RecipeUpdateResponse updateRecipe(RecipeUpdateServiceDto recipeUpdateRequest) {
        Long authUserId = userService.findIdByEmail(recipeUpdateRequest.email());

        postService.updatePost(recipeUpdateRequest.toDomainDto(authUserId));

        return null;
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