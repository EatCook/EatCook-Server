package com.itcook.cooking.api.domains.post.controller;

import com.itcook.cooking.api.domains.post.dto.response.CookTalkFeedsResponse;
import com.itcook.cooking.api.domains.post.service.CookTalkUseCase;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.api.global.dto.PageResponse;
import com.itcook.cooking.domain.common.constant.StatusCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@SecurityRequirement(name = "access-token")
@Tag(name = "02. CookTalk")
public class CookTalkController {

    private final CookTalkUseCase cooktalkUseCase;

    @Operation(summary = "쿡톡 피드 페이징 조회", description = "쿡톡 게시글을 작성일 기준으로 페이징 조회합니다.")
    @GetMapping("/v1/posts/cooktalks/feeds")
    public ResponseEntity<ApiResponse<PageResponse<CookTalkFeedsResponse>>> getCookTalkFeeds(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @ParameterObject @PageableDefault Pageable pageable
    ) {
        PageResponse<CookTalkFeedsResponse> response = cooktalkUseCase.getCookTalkFeeds(
                authenticationUser.getUsername(), pageable);
        return ResponseEntity.status(StatusCode.OK.code)
                .body(ApiResponse.OK(response));
    }

    @Operation(summary = "follower 요청", description = "follower 요청 설명")
    @GetMapping("/following")
    public ResponseEntity<ApiResponse<CookTalkFeedsResponse>> retrieveAllFollowing(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @RequestParam(value = "pageNum", defaultValue = "0") int pageNum
    ) {
        Pageable pageable = PageRequest.of(pageNum, 20, Sort.by(Sort.Direction.DESC, "lastModifiedAt"));
        cooktalkUseCase.getFollowingTalk(authenticationUser.getUsername(), pageable);
        return ResponseEntity.ok(ApiResponse.OK(null));
    }

}
