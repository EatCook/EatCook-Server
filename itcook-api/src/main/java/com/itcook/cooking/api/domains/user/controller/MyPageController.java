package com.itcook.cooking.api.domains.user.controller;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.user.dto.request.MyPageAlertUpdateRequest;
import com.itcook.cooking.api.domains.user.dto.request.MyPageChangePasswordRequest;
import com.itcook.cooking.api.domains.user.dto.request.MyPageProfileChangeRequest;
import com.itcook.cooking.api.domains.user.dto.request.MyPageUpdateProfileRequest;
import com.itcook.cooking.api.domains.user.dto.request.UserUpdateInterestCookRequest;
import com.itcook.cooking.api.domains.user.service.MyPageQueryUseCase;
import com.itcook.cooking.api.domains.user.service.MyPageUseCase;
import com.itcook.cooking.api.domains.user.service.dto.response.MyPageArchivePostsResponse;
import com.itcook.cooking.api.domains.user.service.dto.response.MyPageResponse;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.api.global.dto.PageResponse;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.response.MyRecipeResponse;
import com.itcook.cooking.domain.domains.user.domain.entity.dto.MyPageProfileImageResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageUserInfoResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageSetUpResponse;
import com.itcook.cooking.domain.domains.user.service.dto.response.UserReadInterestCookResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    private final MyPageUseCase myPageUseCase;
    private final MyPageQueryUseCase myPageQueryUseCase;

    @Operation(summary = "마이페이지 유저 정보 조회", description = "엑세스 토큰의 값을 이용해서 해당 유저의 정보를 조회한다")
    @GetMapping("/v1/mypage/user-info")
    public ResponseEntity<ApiResponse<MyPageUserInfoResponse>> getMyPageUserInfo(
        @AuthenticationPrincipal AuthenticationUser authenticationUser
    ) {
        MyPageUserInfoResponse response = myPageQueryUseCase.getMyPageUserInfo(
            authenticationUser.getUsername());
        return ResponseEntity.status(200)
            .body(ApiResponse.OK(response))
            ;
    }

    @Operation(summary = "마이페이지의 마이레시피 조회", description = "페이징 처리로 내가 쓴 게시글들 조회")
    @GetMapping("/v1/mypage/mypage/my-recipe")
    public ResponseEntity<ApiResponse<PageResponse<MyRecipeResponse>>> getMyPageMyRecipe(
        @AuthenticationPrincipal AuthenticationUser authenticationUser,
        @ParameterObject @PageableDefault Pageable pageable
    ) {
        PageResponse<MyRecipeResponse> response = myPageQueryUseCase.getMyPageMyRecipe(
            authenticationUser.getUsername(), pageable);
        return ResponseEntity.status(200)
            .body(ApiResponse.OK(response))
            ;
    }

    @Operation(summary = "비밀번호 변경 요청", description = "현재 비밀번호와 새로운 비밀번호를 받아서 비밀번호 변경 요청")
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

    @Operation(summary = "프로필 편집 요청", description = "닉네임을 받아 프로필 편집 요청을 한")
    @PatchMapping("/v1/mypage/profile")
    public ResponseEntity<ApiResponse> updateProfile(
        @AuthenticationPrincipal AuthenticationUser authenticationUser,
        @RequestBody @Valid MyPageUpdateProfileRequest request
    ) {
        myPageUseCase.updateProfile(request.toServiceDto(authenticationUser.getUsername()));
        return ResponseEntity.status(200)
            .body(ApiResponse.OK("프로필이 편집되었습니다"));
    }

    @Operation(summary = "회원 탈퇴 요청", description = "회원 탈퇴 요청")
    @DeleteMapping("/v1/mypage/profile/leave")
    public ResponseEntity<ApiResponse> leaveUser(
        @AuthenticationPrincipal AuthenticationUser authenticationUser
    ) {
        myPageUseCase.leaveUser(authenticationUser.getUsername());
        return ResponseEntity.status(200)
            .body(ApiResponse.OK("회원 탈퇴하였습니다"))
            ;
    }

    @Operation(summary = "마이페이지 프로필 설정(서비스 알림, 이벤트 알림) 조회 요청", description = "마이페이지 프로필 설정 요청")
    @GetMapping("/v1/mypage/setting")
    public ResponseEntity<ApiResponse<MyPageSetUpResponse>> getMyPageSetting(
        @AuthenticationPrincipal AuthenticationUser authenticationUser
    ) {
        MyPageSetUpResponse myPageSetUpResponse = myPageQueryUseCase.getMyPageSetUp(
            authenticationUser.getUsername());

        return ResponseEntity.ok(ApiResponse.OK(myPageSetUpResponse));
    }

    @Operation(summary = "마이페이지 프로필 설정(서비스 알림, 이벤트 알림) 변경 요청", description = "마이페이지 프로필 설정 변경 요청")
    @PatchMapping("/v1/mypage/setting")
    public ResponseEntity<ApiResponse> updateMyPageSetting(
        @AuthenticationPrincipal AuthenticationUser authenticationUser,
        @RequestBody @Valid MyPageAlertUpdateRequest myPageAlertUpdateRequest
    ) {
        myPageUseCase.updateMyPageSetUp(authenticationUser.getUsername(),
            myPageAlertUpdateRequest.toServiceDto()
        );
        return ResponseEntity.ok(ApiResponse.OK("알림 설정 변경 성공했습니다"));
    }

    @Operation(summary = "마이페이지 설정의 관심 요리 조회", description = "마이페이지 설정의 관심 요리 조회")
    @GetMapping("/v1/mypage/setting/interest-cook")
    public ResponseEntity<ApiResponse<UserReadInterestCookResponse>> getInterestCook(
        @AuthenticationPrincipal AuthenticationUser authenticationUser
    ) {
        UserReadInterestCookResponse response = myPageQueryUseCase.getInterestCook(
            authenticationUser.getUsername());
        return ResponseEntity.ok(ApiResponse.OK(response));
    }

    @Operation(summary = "마이페이지 설정의 관심 요리 업데이트", description = "생활 유형과 요리 유형을 받아 마이페이지 설정의 관심 요리 업데이트")
    @PatchMapping("/v1/mypage/setting/interest-cook")
    public ResponseEntity<ApiResponse> updateInterestCook(
        @AuthenticationPrincipal AuthenticationUser authenticationUser,
        @RequestBody @Valid UserUpdateInterestCookRequest request
    ) {
        myPageUseCase.updateInterestCook(authenticationUser.getUsername(), request.toServiceDto());
        return ResponseEntity.ok(ApiResponse.OK("관심 요리 설정이 변경됐습니다"));
    }

    @Operation(summary = "마이페이지 보관함 조회", description = "토큰의 이메일을로 마이페이지 보관함을 조회한다.")
    @GetMapping("/v1/mypage/archives")
    public ResponseEntity<ApiResponse<List<MyPageArchivePostsResponse>>> getArchivePosts(
        @AuthenticationPrincipal AuthenticationUser authenticationUser
    ) {
        List<MyPageArchivePostsResponse> response = myPageQueryUseCase.getArchivePosts(
            authenticationUser.getUsername());
        return ResponseEntity.status(200)
            .body(ApiResponse.OK(response))
            ;
    }

    @Operation(summary = "마이페이지 프로필 이미지 변경 요청",
        description = "파일 확장자명을 받아서 프로필 이미지를 변경합니다.")
    @PostMapping("/v1/mypage/profile")
    public ResponseEntity<ApiResponse> updateMyPageProfile(
        @AuthenticationPrincipal AuthenticationUser authenticationUser,
        @RequestBody @Valid MyPageProfileChangeRequest myPageProfileChangeRequest
    ) {
        MyPageProfileImageResponse response = myPageUseCase.changeMyPageProfileImage(
            myPageProfileChangeRequest.toServiceDto(
                authenticationUser.getUsername()));
        return ResponseEntity.status(200)
            .body(ApiResponse.OK(response));
    }

}
