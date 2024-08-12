package com.itcook.cooking.api.domains.post.controller;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.user.service.OtherPageUseCase;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.api.global.dto.PageResponse;
import com.itcook.cooking.domain.common.constant.StatusCode;
import com.itcook.cooking.domain.domains.user.service.dto.response.OtherPagePostInfoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "access-token")
@RequestMapping("/api")
@Tag(name = "11. OtherPage")
public class OtherPagePostController {

    private final OtherPageUseCase otherPageUseCase;

    @Operation(summary = "특정 유저 게시글 조회", description = "특정 유저의 게시글 정보 페이징 조회한다.")
    @GetMapping("/v1/other-page/user-info/{otherUserId}/posts")
    public ResponseEntity<ApiResponse<PageResponse<OtherPagePostInfoResponse>>> getOtherPageUserPosts(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @RequestParam(value = "pageNum", defaultValue = "0") int pageNum,
            @PathVariable final Long otherUserId
    ) {
        Pageable pageable = PageRequest.of(pageNum, 20,
                Sort.by(Sort.Direction.DESC, "lastModifiedAt"));
        PageResponse<OtherPagePostInfoResponse> response = otherPageUseCase.getOtherPagePostInfo(
                authenticationUser.getUsername(), otherUserId, pageable);
        return ResponseEntity.status(StatusCode.OK.code)
                .body(ApiResponse.OK(response));
    }
}
