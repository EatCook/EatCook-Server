package com.itcook.cooking.api.domains.user.controller;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.user.dto.request.MyPageChangePasswordRequest;
import com.itcook.cooking.api.domains.user.service.MyPageUserCase;
import com.itcook.cooking.api.domains.user.service.dto.response.MyPageResponse;
import com.itcook.cooking.api.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "access-token")
@RequestMapping("/api")
@Tag(name = "05. MyPage")
public class MyPageController {

    private final MyPageUserCase myPageUserCase;

    @Operation(summary = "마이페이지 조회 요청", description = "마이페이지 조회 요청")
    @GetMapping("/v1/mypage")
    public ResponseEntity<ApiResponse<MyPageResponse>> getMyPage(
        @AuthenticationPrincipal AuthenticationUser authenticationUser
    ) {
        MyPageResponse response = myPageUserCase.getMyPage(authenticationUser.getUsername());
        return ResponseEntity.status(200)
            .body(ApiResponse.OK(response))
            ;
    }

    @Operation(summary = "비밀번호 변경 요청", description = "비밀번호 변경 요청")
    @PostMapping("/v1/mypage/profile/password")
    public ResponseEntity<ApiResponse> changePassword(
        @AuthenticationPrincipal AuthenticationUser authenticationUser,
        @RequestBody @Valid MyPageChangePasswordRequest request
    ) {
        myPageUserCase.changePassword(request.toServiceDto(authenticationUser.getUsername()));
        return ResponseEntity.status(200)
            .body(ApiResponse.OK("비밀번호가 변경되었습니다"))
            ;
    }
}
