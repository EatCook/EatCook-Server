package com.itcook.cooking.api.domains.post.controller;

import com.itcook.cooking.api.domains.post.dto.request.LikedRequest;
import com.itcook.cooking.api.domains.post.service.LikedUseCase;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/liked")
@SecurityRequirement(name = "access-token")
@Tag(name = "04. Liked")
public class LikedController {

    private final LikedUseCase likedUseCase;

    @PostMapping("/add")
    public void likedAdd(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody LikedRequest likedRequest
    ) {
        likedUseCase.likedAdd(authenticationUser.getUsername(), likedRequest.getPostId());
    }

    @PostMapping("/del")
    public void likedDel(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody LikedRequest likedRequest
    ) {
        likedUseCase.likedDel(authenticationUser.getUsername(), likedRequest.getPostId());
    }
}
