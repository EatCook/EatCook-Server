package com.itcook.cooking.api.domains.user.controller;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.user.dto.request.follow.FollowRequest;
import com.itcook.cooking.api.domains.user.service.FollowUseCase;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
@Tag(name = "04. Follow")
public class FollowController {

    private final FollowUseCase followUseCase;

    @PostMapping("/follow")
    public void followUser(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody FollowRequest followRequest
    ) {
        followUseCase.toUserFollowAdd(authenticationUser.getUsername(), followRequest);
    }

    @PostMapping("/unfollow")
    public void unFollowUser(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody FollowRequest followRequest
    ) {
        followUseCase.toUserFollowDelete(authenticationUser.getUsername(), followRequest);
    }
}
