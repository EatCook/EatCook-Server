package com.itcook.cooking.api.domains.home.controller;

import com.itcook.cooking.api.domains.home.dto.response.HomePagingInterestReadResponse;
import com.itcook.cooking.api.domains.home.dto.response.HomePagingSpacialReadResponse;
import com.itcook.cooking.api.domains.home.dto.response.HomeUserCookingThemeReadResponse;
import com.itcook.cooking.api.domains.home.service.HomeUseCase;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home")
@SecurityRequirement(name = "access-token")
@Tag(name = "10. Home")
public class HomeController {

    private final HomeUseCase homeUseCase;

    @Operation(summary = "회원 정보 조회", description = "회원 닉네임, 요리타입, 생활 유형")
    @GetMapping
    public ResponseEntity<ApiResponse<HomeUserCookingThemeReadResponse>> getHomePage(
            @AuthenticationPrincipal AuthenticationUser authenticationUser
    ) {
        HomeUserCookingThemeReadResponse homePage = homeUseCase.getHomePage(authenticationUser.getUsername());

        return ResponseEntity.status(200)
                .body(ApiResponse.OK(homePage));
    }

    @Operation(summary = "요리 타입", description = "요리 타입 페이징")
    @GetMapping("/interest/{cookingTheme}")
    public ResponseEntity<ApiResponse<HomePagingInterestReadResponse>> getPostByCookingTheme(
            @PathVariable String cookingTheme,
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @RequestParam(value = "pageNum", defaultValue = "0") int pageNum
    ) {
        Pageable pageable = PageRequest.of(pageNum - 1, 20, Sort.by(Sort.Direction.DESC, "lastModifiedAt"));
        HomePagingInterestReadResponse getPostByCookingTheme =
                homeUseCase.getPostByCookingTheme(cookingTheme, authenticationUser.getUsername(), pageable);
        return ResponseEntity.status(200)
                .body(ApiResponse.OK(getPostByCookingTheme));
    }

    @Operation(summary = "생활 타입", description = "생활 정보 페이징")
    @GetMapping("/special/{lifeType}")
    public ResponseEntity<ApiResponse<HomePagingSpacialReadResponse>> getPostByLifeType(
            @PathVariable String lifeType,
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @RequestParam(value = "pageNum", defaultValue = "1") int pageNum
    ) {
        Pageable pageable = PageRequest.of(pageNum - 1, 20, Sort.by(Sort.Direction.DESC, "lastModifiedAt"));

        HomePagingSpacialReadResponse getPostByLifeType =
                homeUseCase.getLifeTypeByPost(lifeType, authenticationUser.getUsername(), pageable);
        return ResponseEntity.status(200)
                .body(ApiResponse.OK(getPostByLifeType));
    }

}
