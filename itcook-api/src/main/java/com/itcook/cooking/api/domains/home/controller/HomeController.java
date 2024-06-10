package com.itcook.cooking.api.domains.home.controller;

import com.itcook.cooking.api.domains.home.dto.response.HomeReadResponse;
import com.itcook.cooking.api.domains.home.service.HomeUseCase;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/home")
@SecurityRequirement(name = "access-token")
@Tag(name = "10. Home")
public class HomeController {

    private final HomeUseCase homeUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<HomeReadResponse>> getHomePage(
            @AuthenticationPrincipal AuthenticationUser authenticationUser
    ) {
        HomeReadResponse homePage = homeUseCase.getHomePage(authenticationUser.getUsername());

        return ResponseEntity.status(200)
                .body(ApiResponse.OK(homePage));
    }
}
