package com.itcook.cooking.api.domains.post.controller;

import com.itcook.cooking.api.domains.post.dto.request.LikedRequest;
import com.itcook.cooking.api.domains.post.service.LikedUseCase;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Tag(name = "02. Post")
@SecurityRequirement(name = "access-token")
@RequestMapping("/api/v1/liked")
@RequiredArgsConstructor
@Slf4j
public class LikedController {

    private final LikedUseCase likedUseCase;

    @PostMapping("/add")
    public void likedAdd(
            @Parameter(in = ParameterIn.COOKIE) @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody LikedRequest likedRequest
    ) {
//        likedUseCase.likedAdd(authenticationUser.getUsername(), likedRequest.getPostId());
        likedUseCase.likedAdd("test1@naver.com", likedRequest.getPostId());
    }

    @PostMapping("/del")
    public void likedDel(
            @Parameter(in = ParameterIn.COOKIE) @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody LikedRequest likedRequest
    ) {
//        likedUseCase.likedDel(authenticationUser.getUsername(), likedRequest.getPostId());
        likedUseCase.likedDel("test1@naver.com", likedRequest.getPostId());
    }
}
