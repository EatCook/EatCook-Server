package com.itcook.cooking.api.domains.post.controller;


import com.itcook.cooking.api.domains.post.dto.request.CookTalkRequest;
import com.itcook.cooking.api.domains.post.dto.response.CookTalkResponse;
import com.itcook.cooking.api.domains.post.service.CookTalkUseCase;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/cooktalk")
@RequiredArgsConstructor
@Slf4j
public class CookTalkController {

    private final CookTalkUseCase cooktalkUseCase;

    /**
     * Todo
     * create ?
     * read 완료
     * update ?
     * delete ?
     */
    @GetMapping("/test")
    public String cookTalkVerifyTest() {
        log.info("테스트 cookTalkVerifyTest");
        return "/api/cooktalk/test 접근";
    }

    @Operation(summary = "cooktalk 요청")
    @PostMapping("/cooks")
    public ResponseEntity<ApiResponse<List<CookTalkResponse>>> retrieveAllCookTalk(@AuthenticationPrincipal AuthenticationUser authenticationUser,
                                                                                   @Valid @RequestBody CookTalkRequest cookTalkRequest
    ) {
        List<CookTalkResponse> cookTalkData = cooktalkUseCase.getCookTalk(cookTalkRequest.getEmail());

        return ResponseEntity.ok(ApiResponse.OK(cookTalkData));
    }

    @Operation(summary = "follower 요청", description = "유저정보 - email")
    @PostMapping("/following")
    public ResponseEntity<ApiResponse<List<CookTalkResponse>>> retrieveAllFollowing(@AuthenticationPrincipal AuthenticationUser authenticationUser,
                                                                                    @Valid @RequestBody CookTalkRequest cookTalkRequest) {
        List<CookTalkResponse> followerTalk = cooktalkUseCase.getFollowingTalk(cookTalkRequest.getEmail());
        return ResponseEntity.ok(ApiResponse.OK(followerTalk));
    }

}
