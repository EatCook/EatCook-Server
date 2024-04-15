package com.itcook.cooking.api.domains.user.controller;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.user.dto.request.MyPageAlertUpdateRequest;
import com.itcook.cooking.api.domains.user.dto.request.MyPageChangePasswordRequest;
import com.itcook.cooking.api.domains.user.dto.request.MyPageUpdateProfileRequest;
import com.itcook.cooking.api.domains.user.service.MyPageUseCase;
import com.itcook.cooking.api.domains.user.service.dto.response.MyPageResponse;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageLeaveUser;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageSetUpResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    private final MyPageUseCase myPageUseCase;
    private final UserDomainService userDomainService;

    @Operation(summary = "마이페이지 조회 요청", description = "마이페이지 조회 요청")
    @GetMapping("/v1/mypage")
    public ResponseEntity<ApiResponse<MyPageResponse>> getMyPage(
        @AuthenticationPrincipal AuthenticationUser authenticationUser
    ) {
        MyPageResponse response = myPageUseCase.getMyPage(authenticationUser.getUsername());
        return ResponseEntity.status(200)
            .body(ApiResponse.OK(response))
            ;
    }

    @Operation(summary = "비밀번호 변경 요청", description = "비밀번호 변경 요청")
    @PatchMapping("/v1/mypage/profile/password")
    public ResponseEntity<ApiResponse> changePassword(
        @AuthenticationPrincipal AuthenticationUser authenticationUser,
        @RequestBody @Valid MyPageChangePasswordRequest request
    ) {
        myPageUseCase.changePassword(request.toServiceDto(authenticationUser.getUsername()));
        return ResponseEntity.status(200)
            .body(ApiResponse.OK("비밀번호가 변경되었습니다"))
            ;
    }

    @Operation(summary = "프로필 편집 요청", description = "프로필 편집 요청")
    @PatchMapping("/v1/mypage/profile")
    public ResponseEntity<ApiResponse> updateProfile(
        @AuthenticationPrincipal AuthenticationUser authenticationUser,
        @RequestBody @Valid MyPageUpdateProfileRequest request
    ) {
        userDomainService.updateProfile(request.toServiceDto(authenticationUser.getUsername()));
        return ResponseEntity.status(200)
            .body(ApiResponse.OK("프로필이 편집되었습니다"));
    }

    @Operation(summary = "회원 탈퇴 요청", description = "회원 탈퇴 요청")
    @DeleteMapping("/v1/mypage/profile/leave")
    public ResponseEntity<ApiResponse> leaveUser(
        @AuthenticationPrincipal AuthenticationUser authenticationUser
    ) {
        userDomainService.leaveUser(MyPageLeaveUser.of(authenticationUser.getUsername()));
        return ResponseEntity.status(200)
            .body(ApiResponse.OK("회원 탈퇴하였습니다"))
            ;
    }

    @Operation(summary = "마이페이지 프로필 설정 요청", description = "마이페이지 프로필 설정 요청")
    @GetMapping("/v1/mypage/setting")
    public ResponseEntity<ApiResponse<MyPageSetUpResponse>> getMyPageSetting(
        @AuthenticationPrincipal AuthenticationUser authenticationUser
    ) {
        MyPageSetUpResponse myPageSetUpResponse = userDomainService.getMyPageSetUp(
            authenticationUser.getUsername());

        return ResponseEntity.ok(ApiResponse.OK(myPageSetUpResponse));
    }

    @Operation(summary = "마이페이지 프로필 설정 변경 요청", description = "마이페이지 프로필 설정 변경 요청")
    @PatchMapping("/v1/mypage/setting")
    public ResponseEntity<ApiResponse> updateMyPageSetting(
        @AuthenticationPrincipal AuthenticationUser authenticationUser,
        @RequestBody @Valid MyPageAlertUpdateRequest myPageAlertUpdateRequest
    ) {
        userDomainService.updateMyPageSetUp(authenticationUser.getUsername(),
            myPageAlertUpdateRequest.toServiceDto()
        );
        return ResponseEntity.ok(ApiResponse.OK("알림 설정 변경 성공했습니다"));
    }
}
