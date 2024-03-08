package com.itcook.cooking.api.domains.post.controller;

import com.itcook.cooking.api.domains.post.dto.request.RecipeCreateRequest;
import com.itcook.cooking.api.domains.post.dto.request.RecipeDeleteRequest;
import com.itcook.cooking.api.domains.post.dto.request.RecipeReadRequest;
import com.itcook.cooking.api.domains.post.dto.request.RecipeUpdateRequest;
import com.itcook.cooking.api.domains.post.dto.response.RecipeResponse;
import com.itcook.cooking.api.domains.post.service.RecipeUseCase;
import com.itcook.cooking.api.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "03. Recipe")
@SecurityRequirement(name = "access-token")
@RequestMapping("/api/v1/recipe")
@RequiredArgsConstructor
@Slf4j
public class RecipeController {

    private final RecipeUseCase recipeUseCase;

    @Operation(summary = "recipe 테스트 요청", description = "recipe 테스트 요청 설명")
    @GetMapping("/test")
    public String testRecipe() {
        log.info("테스트 userVerifyTest");
        return "테스트 성공";
    }

    @Operation(summary = "recipe 생성 요청", description = "recipe 생성 요청 설명")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createRecipe(@Valid @RequestBody RecipeCreateRequest recipeCreateRequest) {
        log.info("createRecipe");

        recipeUseCase.createRecipe(recipeCreateRequest);
        return ResponseEntity.status(200)
                .body(ApiResponse.OK("레시피 등록 완료"));
    }

    @Operation(summary = "recipe 조회 요청", description = "recipe 조회 요청 설명")
    @PostMapping("/read")
    public ResponseEntity<ApiResponse<RecipeResponse>> readRecipe(@Valid @RequestBody RecipeReadRequest recipeReadRequest) {
        log.info("readRecipe");

        RecipeResponse recipeResponses = recipeUseCase.getReadRecipe(recipeReadRequest);
        return ResponseEntity.status(200)
                .body(ApiResponse.OK(recipeResponses));
    }

    @Operation(summary = "recipe 수정 요청", description = "recipe 수정 요청 설명")
    @PostMapping("/update")
    public ResponseEntity<ApiResponse> updateRecipe(@Valid @RequestBody RecipeUpdateRequest recipeUpdateRequest) {
        log.info("updateRecipe");

        recipeUseCase.updateRecipe(recipeUpdateRequest);

        return ResponseEntity.status(200)
                .body(ApiResponse.OK("레시피 수정 완료"));
    }

    @Operation(summary = "recipe 삭제 요청", description = "recipe 삭제 요청 설명")
    @PostMapping("/delete")
    public void deleteRecipe(@Valid @RequestBody RecipeDeleteRequest recipeDeleteRequest) {
        log.info("deleteRecipe");

        recipeUseCase.deleteRecipe(recipeDeleteRequest);

    }

}
