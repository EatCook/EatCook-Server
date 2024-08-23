package com.itcook.cooking.api.domains.user.controller;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.user.dto.request.ArchiveRequest;
import com.itcook.cooking.api.domains.user.service.ArchiveUseCase;
import com.itcook.cooking.api.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@SecurityRequirement(name = "access-token")
@Tag(name = "06. Archive")
public class ArchiveController {

    private final ArchiveUseCase archiveUseCase;

    @PostMapping("/v1/archive")
    public ResponseEntity<ApiResponse<String>> archiveAdd(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody ArchiveRequest archiveRequest
    ) {
        archiveUseCase.archiveAdd(authenticationUser.getUsername(), archiveRequest.getPostId());
        return ResponseEntity.status(200)
                .body(ApiResponse.OK("저장 되었습니다."));
    }

    @DeleteMapping("/v1/archive")
    public ResponseEntity<ApiResponse<String>> archiveDel(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody ArchiveRequest archiveRequest
    ) {
        archiveUseCase.archiveDel(authenticationUser.getUsername(), archiveRequest.getPostId());

        return ResponseEntity.status(200)
                .body(ApiResponse.OK("삭제 되었습니다."));
    }
}
