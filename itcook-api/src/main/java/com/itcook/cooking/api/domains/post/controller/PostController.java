package com.itcook.cooking.api.domains.post.controller;


import com.itcook.cooking.api.domains.post.dto.request.PostRequest;
import com.itcook.cooking.api.domains.post.dto.response.PostCookTalkResponse;
import com.itcook.cooking.api.domains.post.service.PostFacadeService;
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
@RequestMapping("/api/v1/post")
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostFacadeService postFacadeService;

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
    @PostMapping("/cooktalk/all")
    public ResponseEntity<ApiResponse<List<PostCookTalkResponse>>> retrieveAllCookTalk(@AuthenticationPrincipal AuthenticationUser authenticationUser,
                                                                                       @Valid @RequestBody PostRequest postRequest
    ) {
        List<PostCookTalkResponse> cookTalkData = postFacadeService.getCookTalk(postRequest.getEmail());

        return ResponseEntity.ok(ApiResponse.OK(cookTalkData));
    }

    @Operation(summary = "follower 요청", description = "유저정보 - email")
    @PostMapping("/follower/all")
    public ResponseEntity<ApiResponse<List<PostCookTalkResponse>>> retrieveAllFollower(@AuthenticationPrincipal AuthenticationUser authenticationUser,
                                                                                       @Valid @RequestBody PostRequest postRequest) {
        List<PostCookTalkResponse> followerTalk = postFacadeService.getFollowerTalk(postRequest.getEmail());
        return ResponseEntity.ok(ApiResponse.OK(followerTalk));
    }


}
