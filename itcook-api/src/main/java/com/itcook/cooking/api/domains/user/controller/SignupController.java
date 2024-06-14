package com.itcook.cooking.api.domains.user.controller;

import com.itcook.cooking.api.domains.user.dto.request.AddSignupRequest;
import com.itcook.cooking.api.domains.user.dto.request.FindUserRequest;
import com.itcook.cooking.api.domains.user.dto.request.SendEmailAuthRequest;
import com.itcook.cooking.api.domains.user.dto.request.SignupRequest;
import com.itcook.cooking.api.domains.user.dto.request.VerifyEmailAuthRequest;
import com.itcook.cooking.api.domains.user.dto.request.VerifyFindUserRequest;
import com.itcook.cooking.api.domains.user.dto.response.AddUserResponse;
import com.itcook.cooking.api.domains.user.dto.response.UserResponse;
import com.itcook.cooking.api.domains.user.service.SignupUseCase;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.domain.domains.user.entity.dto.AddSignupDomainResponse;
import com.itcook.cooking.domain.domains.user.service.dto.AddSignupDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "access-token")
@RequestMapping("/api")
@Slf4j
@Tag(name = "01. User")
public class SignupController {

    private final SignupUseCase signupUseCase;

    @Operation(summary = "이메일 인증 요청", description = "가입할 이메일 인증 요청")
    @PostMapping("/v1/emails/request")
    public ResponseEntity<ApiResponse> sendEmailAuthRequest(
        @RequestBody @Valid SendEmailAuthRequest sendEmailAuthRequest
    ) {
        signupUseCase.sendAuthCodeSignup(sendEmailAuthRequest.toServiceDto());
        return ResponseEntity.status(200)
            .body(ApiResponse.OK("이메일 인증 요청 성공."));
    }


    @Operation(summary = "이메일 검증 요청", description = "가입할 이메일과 인증코드를 검증하는 요청")
    @PostMapping("/v1/emails/verify")
    public ResponseEntity<ApiResponse> verifyEmailAuth(
        @RequestBody @Valid VerifyEmailAuthRequest verifyEmailAuthRequest
    ) {
        signupUseCase.verifyAuthCode(verifyEmailAuthRequest.toServiceDto());
        return ResponseEntity.status(200)
            .body(ApiResponse.OK("이메일 인증 검증 성공."));
    }

    @Operation(summary = "회원가입 요청", description = "이메일과 비밀번호를 받아 회원가입을 요청")
    @PostMapping("/v1/users")
    public ResponseEntity<ApiResponse<UserResponse>> signup(
        @RequestBody @Valid SignupRequest signupRequest
    ) {
        UserResponse userResponse = signupUseCase.signup(signupRequest);
        return ResponseEntity.ok(ApiResponse.OK(userResponse));
    }

    @Operation(summary = "회원가입 추가 요청", description = "회원가입 추가 요청")
    @PostMapping("/v1/users/add-signup")
    public ResponseEntity<ApiResponse<AddSignupDomainResponse>> addSignup(
        @RequestBody @Valid AddSignupRequest addSignupRequest
    ) {
        AddSignupDomainResponse addSignupDomainResponse = signupUseCase.addSignup(
            addSignupRequest.toServiceDto());
        return ResponseEntity.status(200)
            .body(ApiResponse.OK(addSignupDomainResponse));
    }

    /**
     * 계정 찾기 인증 요청
     */
    @Operation(summary = "계정 찾기 인증 요청", description = "이메일을 받아 계정 찾기 인증 요청")
    @PostMapping("/v1/users/find")
    public ResponseEntity<ApiResponse> findUser(
        @RequestBody @Valid FindUserRequest findUserRequest
    ) {
        signupUseCase.findUser(findUserRequest.toServiceDto());
        return ResponseEntity.ok(ApiResponse.OK("계정 찾기 인증 요청 성공"));
    }

    /**
     * 계정 찾기 인증 코드 검증 (메일로 임시 비밀번호 발급)
     */
    @Operation(summary = "계정 찾기 인증코드 검증", description = "계정 찾기 인증 코드 검증 (메일로 임시 비밀번호 발급)")
    @PostMapping("/v1/users/find/verify")
    public ResponseEntity<ApiResponse> verifyFindUser(
        @RequestBody @Valid VerifyFindUserRequest verifyFindUserRequest
    ) {
        signupUseCase.verifyFindUser(
            verifyFindUserRequest.toServiceDto());
        return ResponseEntity.ok(ApiResponse.OK("메일로 임시 비밀번호 발급되었습니다."));
    }


}
