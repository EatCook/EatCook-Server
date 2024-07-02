package com.itcook.cooking.api.domains.user.controller;

import static com.itcook.cooking.api.global.consts.ItCookConstants.ACCESS_TOKEN_HEADER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.REFRESH_TOKEN_HEADER;

import com.itcook.cooking.api.domains.user.dto.request.SocialLoginRequest;
import com.itcook.cooking.api.domains.user.service.LoginUseCase;
import com.itcook.cooking.api.domains.user.service.dto.response.SocialLoginResponse;
import com.itcook.cooking.api.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/oauth2/login")
@Tag(name = "01-1. Social Login")
public class AuthController {

    private final LoginUseCase loginUseCase;

    @PostMapping
    public ResponseEntity socialLogin(
        @RequestBody SocialLoginRequest socialLoginRequest
    ) {
        SocialLoginResponse response = loginUseCase.socialLogin(socialLoginRequest.of());
        return ResponseEntity.status(200)
            .header(ACCESS_TOKEN_HEADER, response.accessToken())
            .header(REFRESH_TOKEN_HEADER, response.refreshToken())
            .body(ApiResponse.OK("로그인 성공하였습니다."))
            ;
    }
}
