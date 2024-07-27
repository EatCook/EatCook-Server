package com.itcook.cooking.api.domains.user.controller;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.user.service.OtherPageUseCase;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.domain.common.constant.StatusCode;
import com.itcook.cooking.domain.domains.user.service.dto.response.OtherPageUserInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "access-token")
@RequestMapping("/api")
@Tag(name = "11. OtherPage")
public class OtherPageController {

    private final OtherPageUseCase otherPageUseCase;

    @Operation(summary = "특정 유저 정보 조회", description = "특정 유저의 정보를 조회한다.")
    @GetMapping("/v1/other-page/user-info/{otherUserId}")
    public ResponseEntity<ApiResponse<OtherPageUserInfoResponse>> getOtherPageUserInfo(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @PathVariable final Long otherUserId
    ) {
        OtherPageUserInfoResponse response = otherPageUseCase.getOtherPageUserInfo(
                authenticationUser.getUsername(), otherUserId
        );

        return ResponseEntity.status(StatusCode.OK.code)
                .body(ApiResponse.OK(response));
    }

}
