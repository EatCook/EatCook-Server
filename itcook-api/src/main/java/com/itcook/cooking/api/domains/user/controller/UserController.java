package com.itcook.cooking.api.domains.user.controller;

import com.itcook.cooking.api.domains.user.dto.request.SendEmailAuthRequest;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.domain.domains.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {

    private final UserService userService;

    @GetMapping("/test")
    public String userVerifyTest() {
        log.info("테스트 userVerifyTest");
        return "테스트 성공";
    }

    @PostMapping("/v1/emails/request")
    public ResponseEntity<ApiResponse> sendEmailAuthRequest(@RequestBody SendEmailAuthRequest sendEmailAuthRequest) {
        userService.sendAuthCode(sendEmailAuthRequest.toServiceRequest());
        return ResponseEntity.status(200)
            .body(ApiResponse.OK("이메일 인증 요청 성공."));
    }

}
