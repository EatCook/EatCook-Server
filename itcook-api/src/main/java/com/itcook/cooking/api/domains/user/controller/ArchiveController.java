package com.itcook.cooking.api.domains.user.controller;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.user.dto.request.ArchiveRequest;
import com.itcook.cooking.api.domains.user.service.ArchiveUseCase;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/archive")
@Slf4j
@Tag(name = "01. User")
public class ArchiveController {

    private final ArchiveUseCase archiveUseCase;

    @PostMapping("/add")
    public void archiveAdd(
            @Parameter(in = ParameterIn.COOKIE) @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody ArchiveRequest archiveRequest
    ) {
        archiveUseCase.archiveAdd(authenticationUser.getUsername(), archiveRequest.getPostId());
    }

    @PostMapping("/del")
    public void archiveDel(
            @Parameter(in = ParameterIn.COOKIE) @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody ArchiveRequest archiveRequest
    ) {
        archiveUseCase.archiveDel(authenticationUser.getUsername(), archiveRequest.getPostId());
    }
}
