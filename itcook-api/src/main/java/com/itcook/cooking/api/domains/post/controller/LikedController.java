package com.itcook.cooking.api.domains.post.controller;

import com.itcook.cooking.api.domains.post.dto.request.LikedRequest;
import com.itcook.cooking.api.domains.post.service.LikedUseCase;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.itcook.cooking.domain.common.constant.StatusCode.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@SecurityRequirement(name = "access-token")
@Tag(name = "08. Liked")
public class LikedController {

    private final LikedUseCase likedUseCase;

    @PostMapping("/v1/liked")
    public ResponseEntity<ApiResponse<String>> likedAdd(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody LikedRequest likedRequest
    ) {
        likedUseCase.likedAdd(authenticationUser.getUsername(), likedRequest.getPostId());

        return ResponseEntity.status(OK.code)
                .body(ApiResponse.OK("해당 게시물의 좋아요를 저장하였습니다."));
    }

    @DeleteMapping("/v1/liked/{postId}")
    public ResponseEntity<ApiResponse> likedDel(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @PathVariable Long postId
    ) {
        likedUseCase.likedDel(authenticationUser.getUsername(), postId);

        return ResponseEntity.status(OK.code)
                .body(ApiResponse.OK("해당 게시물의 좋아요를 삭제하였습니다."));
    }
}
