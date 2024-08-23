package com.itcook.cooking.api.domains.user.controller;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.user.dto.request.FollowRequest;
import com.itcook.cooking.api.domains.user.service.FollowUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@SecurityRequirement(name = "access-token")
@Tag(name = "07. Follow")
public class FollowController {

    private final FollowUseCase followUseCase;

    @Operation(summary = "팔로우 등록 요청", description = "팔로우할 유저를 등록합니다.")
    @PostMapping("/follow")
    public void followUser(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody FollowRequest followRequest
    ) {
        followUseCase.followUser(authenticationUser.getUsername(), followRequest);
    }

    @Operation(summary = "팔로우 취소 요청", description = "팔로우한 유저를 삭제합니다.")
    @DeleteMapping("/follow/{toUserId}")
    public void unFollowUser(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @PathVariable Long toUserId
    ) {
        followUseCase.unFollowUser(authenticationUser.getUsername(), toUserId);
    }
}
