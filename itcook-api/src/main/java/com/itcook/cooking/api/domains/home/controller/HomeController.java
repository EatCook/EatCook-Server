package com.itcook.cooking.api.domains.home.controller;

import com.itcook.cooking.api.domains.home.dto.response.HomePagingInterestReadResponse;
import com.itcook.cooking.api.domains.home.dto.response.HomePagingLifeTypeReadResponse;
import com.itcook.cooking.api.domains.home.dto.response.HomeUserCookingThemeReadResponse;
import com.itcook.cooking.api.domains.home.service.HomeUseCase;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.global.dto.ApiResponse;
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

    @GetMapping
    public ResponseEntity<ApiResponse<HomeUserCookingThemeReadResponse>> getHomePage(
            @AuthenticationPrincipal AuthenticationUser authenticationUser
    ) {
        HomeUserCookingThemeReadResponse homePage = homeUseCase.getHomePage(authenticationUser.getUsername());

        return ResponseEntity.status(200)
                .body(ApiResponse.OK(homePage));
    }

    @GetMapping("/interest/{cookingTheme}")
    public ResponseEntity<ApiResponse<HomePagingInterestReadResponse>> getPostByCookingTheme(
            @PathVariable String cookingTheme,
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @RequestParam(value = "pageNum", defaultValue = "0") int pageNum
    ) {
        Pageable pageable = PageRequest.of(pageNum, 20, Sort.by(Sort.Direction.DESC, "lastModifiedAt"));
        HomePagingInterestReadResponse getPostByCookingTheme =
                homeUseCase.getPostByCookingTheme(cookingTheme, authenticationUser.getUsername(), pageable);
        return ResponseEntity.status(200)
                .body(ApiResponse.OK(getPostByCookingTheme));
    }

}
