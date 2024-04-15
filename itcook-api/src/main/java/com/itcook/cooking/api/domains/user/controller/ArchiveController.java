package com.itcook.cooking.api.domains.user.controller;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.user.dto.request.ArchiveRequest;
import com.itcook.cooking.api.domains.user.service.ArchiveUseCase;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/archive")
@SecurityRequirement(name = "access-token")
@Tag(name = "04. Archive")
public class ArchiveController {

    private final ArchiveUseCase archiveUseCase;

    @PostMapping("/add")
    public void archiveAdd(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody ArchiveRequest archiveRequest
    ) {
        archiveUseCase.archiveAdd(authenticationUser.getUsername(), archiveRequest.getPostId());
    }

    @PostMapping("/del")
    public void archiveDel(
            @AuthenticationPrincipal AuthenticationUser authenticationUser,
            @Valid @RequestBody ArchiveRequest archiveRequest
    ) {
        archiveUseCase.archiveDel(authenticationUser.getUsername(), archiveRequest.getPostId());
    }
}
