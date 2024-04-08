package com.itcook.cooking.api.domains.user.controller;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.user.service.MyPageUserCase;
import com.itcook.cooking.api.domains.user.service.dto.MyPageResponse;
import com.itcook.cooking.api.global.dto.ApiResponse;
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
@RequestMapping("/api")
@Tag(name = "05. MyPage")
public class MyPageController {

    private final MyPageUserCase myPageUserCase;

    @GetMapping("/v1/mypage")
    public ResponseEntity<ApiResponse<MyPageResponse>> getMyPage(
        @AuthenticationPrincipal AuthenticationUser authenticationUser
    ) {
        MyPageResponse response = myPageUserCase.getMyPage(authenticationUser.getUsername());
        return ResponseEntity.status(200)
            .body(ApiResponse.OK(response))
            ;
    }
}
