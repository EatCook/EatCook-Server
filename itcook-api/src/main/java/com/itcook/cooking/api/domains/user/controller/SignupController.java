package com.itcook.cooking.api.domains.user.controller;

import com.itcook.cooking.api.domains.user.dto.request.SendEmailAuthRequest;
import com.itcook.cooking.api.domains.user.dto.request.VerifyEmailAuthRequest;
import com.itcook.cooking.api.domains.user.service.SignupUseCase;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/open-api")
@Slf4j
@Tag(name = "User")
public class SignupController {

    private final SignupUseCase signupUseCase;

    @GetMapping("/test")
    public String userVerifyTest() {
        log.info("테스트 userVerifyTest");
        return "테스트 성공";
    }

    @PostMapping("/v1/emails/request")
    public ResponseEntity<ApiResponse> sendEmailAuthRequest(
        @RequestBody SendEmailAuthRequest sendEmailAuthRequest
    ) {
        signupUseCase.sendAuthCode(sendEmailAuthRequest);
        return ResponseEntity.status(200)
            .body(ApiResponse.OK("이메일 인증 요청 성공."));
    }


    @PostMapping("/v1/emails/verify")
    public ResponseEntity<ApiResponse> verifyEmailAuth(
        @RequestBody VerifyEmailAuthRequest verifyEmailAuthRequest
    ) {
        signupUseCase.verifyAuthCode(verifyEmailAuthRequest);
        return ResponseEntity.status(200)
            .body(ApiResponse.OK("이메일 인증 검증 성공."));
    }

}
