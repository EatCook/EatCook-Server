package com.itcook.cooking.api.domains.post.controller;


import com.itcook.cooking.api.domains.post.dto.request.CookTalkRequest;
import com.itcook.cooking.api.domains.post.dto.response.CookTalkResponse;
import com.itcook.cooking.api.domains.post.service.CookTalkUseCase;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "02. CookTalk")
@SecurityRequirement(name = "access-token")
@RequestMapping("/api/v1/cooktalk")
@RequiredArgsConstructor
@Slf4j
public class CookTalkController {

    private final CookTalkUseCase cooktalkUseCase;

    @GetMapping("/test")
    public String cookTalkVerifyTest() {
        log.info("테스트 cookTalkVerifyTest");
        return "/api/cooktalk/test 접근";
    }

    @Operation(summary = "cooktalk 요청", description = "cooktalk 요청 설명")
    @PostMapping("/feed")
    public ResponseEntity<ApiResponse<List<CookTalkResponse>>> retrieveAllCookTalk(@AuthenticationPrincipal AuthenticationUser authenticationUser,
                                                                                   @Valid @RequestBody CookTalkRequest cookTalkRequest
    ) {
        List<CookTalkResponse> cookTalkData = cooktalkUseCase.getCookTalkFeed(cookTalkRequest.getEmail());

        return ResponseEntity.ok(ApiResponse.OK(cookTalkData));
    }

    @Operation(summary = "follower 요청", description = "follower 요청 설명")
    @PostMapping("/following")
    public ResponseEntity<ApiResponse<List<CookTalkResponse>>> retrieveAllFollowing(@AuthenticationPrincipal AuthenticationUser authenticationUser,
                                                                                    @Valid @RequestBody CookTalkRequest cookTalkRequest) {
        List<CookTalkResponse> followerTalk = cooktalkUseCase.getFollowingTalk(cookTalkRequest.getEmail());
        return ResponseEntity.ok(ApiResponse.OK(followerTalk));
    }

}
