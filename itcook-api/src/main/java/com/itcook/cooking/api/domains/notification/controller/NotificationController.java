package com.itcook.cooking.api.domains.notification.controller;

import com.itcook.cooking.api.domains.notification.service.NotificationUseCase;
import com.itcook.cooking.api.domains.notification.service.dto.response.NotificationResponse;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@SecurityRequirement(name = "access-token")
public class NotificationController {

    private final NotificationUseCase notificationUseCase;

    @Operation(summary = "알림 조회 요청", description = "토큰을 통해서 해당 유저의 알림 목록을 조회한다.")
    @GetMapping("/v1/notifications")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> notification(
        @AuthenticationPrincipal AuthenticationUser authenticationUser
    ) {
        List<NotificationResponse> response = notificationUseCase.getNotifications(
            authenticationUser.getUsername());
        return ResponseEntity.ok(ApiResponse.OK(response));
    }

}
