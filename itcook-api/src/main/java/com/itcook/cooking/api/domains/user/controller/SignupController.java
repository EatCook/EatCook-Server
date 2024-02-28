package com.itcook.cooking.api.domains.user.controller;

import com.itcook.cooking.api.domains.user.dto.request.SendEmailAuthRequest;
import com.itcook.cooking.api.domains.user.dto.request.SignupRequest;
import com.itcook.cooking.api.domains.user.dto.request.VerifyEmailAuthRequest;
import com.itcook.cooking.api.domains.user.dto.response.UserResponse;
import com.itcook.cooking.api.domains.user.service.SignupUseCase;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
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

    @Operation(summary = "이메일 인증 요청", description = "이메일 인증 요청")
    @PostMapping("/v1/emails/request")
    public ResponseEntity<ApiResponse> sendEmailAuthRequest(
        @RequestBody @Valid SendEmailAuthRequest sendEmailAuthRequest
    ) {
        signupUseCase.sendAuthCode(sendEmailAuthRequest);
        return ResponseEntity.status(200)
            .body(ApiResponse.OK("이메일 인증 요청 성공."));
    }


    @Operation(summary = "이메일 검증 요청",description = "이메일 검증 요청")
    @PostMapping("/v1/emails/verify")
    public ResponseEntity<ApiResponse> verifyEmailAuth(
        @RequestBody @Valid VerifyEmailAuthRequest verifyEmailAuthRequest
    ) {
        signupUseCase.verifyAuthCode(verifyEmailAuthRequest);
        return ResponseEntity.status(200)
            .body(ApiResponse.OK("이메일 인증 검증 성공."));
    }

    @Operation(summary = "회원가입 요청", description = "회원가입 요청")
    @PostMapping("/v1/users")
    public ResponseEntity<ApiResponse<UserResponse>> signup(
        @RequestBody @Valid SignupRequest signupRequest
    ) {
        UserResponse userResponse = signupUseCase.signup(signupRequest);
        return ResponseEntity.ok(ApiResponse.OK(userResponse));
    }

}
