package com.itcook.cooking.api.domains.post.controller;

import com.itcook.cooking.api.domains.post.dto.request.CookTalkRequest;
import com.itcook.cooking.api.domains.post.dto.response.CookTalkResponse;
import com.itcook.cooking.api.domains.post.service.CookTalkUseCase;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "02. CookTalk")
@SecurityRequirement(name = "access-token")
@RequestMapping("/api/v1/cooktalk")
@RequiredArgsConstructor
@Slf4j
public class CookTalkController {

    private final CookTalkUseCase cooktalkUseCase;

    @Operation(summary = "cooktalk 요청", description = "cooktalk 요청 설명")
    @GetMapping("/feed")
    public ResponseEntity<ApiResponse<CookTalkResponse>> retrieveAllCookTalk(
            @Parameter(in = ParameterIn.COOKIE) @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @RequestParam(value = "pageNum", defaultValue = "0") int pageNum
    ) {
        Pageable pageable = PageRequest.of(pageNum, 20, Sort.by(Sort.Direction.DESC, "lastModifiedAt"));
        CookTalkResponse cookTalkResponseData = cooktalkUseCase.getCookTalkFeed(authenticationUser.getUsername(), pageable);

        return ResponseEntity.ok(ApiResponse.OK(cookTalkResponseData));
    }

    @Operation(summary = "follower 요청", description = "follower 요청 설명")
    @GetMapping("/following")
    public ResponseEntity<ApiResponse<CookTalkResponse>> retrieveAllFollowing(
            @Parameter(in = ParameterIn.COOKIE) @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @RequestParam(value = "pageNum", defaultValue = "0") int pageNum
    ) {
        Pageable pageable = PageRequest.of(pageNum, 20, Sort.by(Sort.Direction.DESC, "lastModifiedAt"));
        CookTalkResponse followerTalk = cooktalkUseCase.getFollowingTalk(authenticationUser.getUsername(), pageable);
        return ResponseEntity.ok(ApiResponse.OK(followerTalk));
    }

}
