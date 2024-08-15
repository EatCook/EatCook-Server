package com.itcook.cooking.api.domains.post.controller;

import com.itcook.cooking.api.domains.post.dto.request.RecipeCreateRequest;
import com.itcook.cooking.api.domains.post.dto.request.RecipeUpdateRequest;
import com.itcook.cooking.api.domains.post.dto.response.RecipeCreateResponse;
import com.itcook.cooking.api.domains.post.dto.response.RecipeGetResponse;
import com.itcook.cooking.api.domains.post.dto.response.RecipeUpdateResponse;
import com.itcook.cooking.api.domains.post.service.RecipeUseCase;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.domain.common.constant.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@SecurityRequirement(name = "access-token")
@Tag(name = "03. Recipe")
public class RecipeController {

    private final RecipeUseCase recipeUseCase;

    @Operation(summary = "레시피 등록 요청", description = "쿡톡에 레시피를 등록합니다.")
    @PostMapping("/v1/recipes")
    public ResponseEntity<ApiResponse<RecipeCreateResponse>> createRecipe(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody RecipeCreateRequest recipeCreateRequest
    ) {
        RecipeCreateResponse recipeCreateResponse = recipeUseCase.createRecipe(
                authenticationUser.getUsername(), recipeCreateRequest);

        return ResponseEntity.status(StatusCode.OK.code)
                .body(ApiResponse.OK(recipeCreateResponse));
    }

    @Operation(summary = "레시피 조회 요청", description = "쿡톡에 등록된 특정 레시피를 조회합니다.")
    @GetMapping("/v1/recipes/{recipeId}")
    public ResponseEntity<ApiResponse<RecipeGetResponse>> readRecipe(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @PathVariable Long recipeId
    ) {
        RecipeGetResponse response = recipeUseCase
                .getReadRecipe(authenticationUser.getUsername(), recipeId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.OK(response));
    }

    @Operation(summary = "레시피 수정 요청", description = "쿡톡에 등록된 레시피를 수정합니다.")
    @PatchMapping("/v1/recipes/{recipeId}")
    public ResponseEntity<ApiResponse<RecipeUpdateResponse>> updateRecipe(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody RecipeUpdateRequest recipeUpdateRequest,
            @PathVariable Long recipeId
    ) {

        RecipeUpdateResponse recipeUpdateResponse = recipeUseCase
                .updateRecipe(recipeUpdateRequest, recipeId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.OK(recipeUpdateResponse));
    }

    @Operation(summary = "레시피 삭제 요청", description = "쿡톡에 등록된 레시피를 삭제합니다.")
    @DeleteMapping("/v1/recipes/{recipeId}")
    public ResponseEntity<ApiResponse<String>> removeRecipe(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @PathVariable Long recipeId
    ) {
        recipeUseCase.removeRecipe(authenticationUser.getUsername(), recipeId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.OK("삭제 되었습니다."));
    }

}
