package com.itcook.cooking.api.domains.post.controller;

import com.itcook.cooking.api.domains.post.dto.request.RecipeCreateRequest;
import com.itcook.cooking.api.domains.post.dto.request.RecipeDeleteRequest;
import com.itcook.cooking.api.domains.post.dto.request.RecipeUpdateRequest;
import com.itcook.cooking.api.domains.post.dto.response.RecipeCreateResponse;
import com.itcook.cooking.api.domains.post.dto.response.RecipeReadResponse;
import com.itcook.cooking.api.domains.post.dto.response.RecipeUpdateResponse;
import com.itcook.cooking.api.domains.post.service.RecipeUseCase;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recipe")
@SecurityRequirement(name = "access-token")
@Tag(name = "03. Recipe")
public class RecipeController {

    private final RecipeUseCase recipeUseCase;

    @Operation(summary = "recipe 생성 요청", description = "recipe 생성 요청 설명")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<RecipeCreateResponse>> createRecipe(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody RecipeCreateRequest recipeCreateRequest

    ) {

        RecipeCreateResponse recipeCreateResponse = recipeUseCase.createRecipe(
                authenticationUser.getUsername(),
                recipeCreateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.OK(recipeCreateResponse));
    }

    @Operation(summary = "recipe 조회 요청", description = "recipe 조회 요청 설명")
    @GetMapping("/read")
    public ResponseEntity<ApiResponse<RecipeReadResponse>> readRecipe(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @RequestParam("postId") Long postId
    ) {

        RecipeReadResponse recipeResponses = recipeUseCase.getReadRecipeV1(
                authenticationUser.getUsername(),
                postId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.OK(recipeResponses));
    }

    @Operation(summary = "recipe 수정 요청", description = "recipe 수정 요청 설명")
    @PostMapping("/update")
    public ResponseEntity<ApiResponse<RecipeUpdateResponse>> updateRecipe(
            @Valid @RequestBody RecipeUpdateRequest recipeUpdateRequest
    ) {

        RecipeUpdateResponse recipeUpdateResponse = recipeUseCase.updateRecipe(recipeUpdateRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.OK(recipeUpdateResponse));
    }

    @Operation(summary = "recipe 삭제 요청", description = "recipe 삭제 요청 설명")
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse<String>> deleteRecipe(
            @Valid @RequestBody RecipeDeleteRequest recipeDeleteRequest
    ) {

        recipeUseCase.deleteRecipe(recipeDeleteRequest);

        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.OK("삭제 되었습니다."));
    }

}
