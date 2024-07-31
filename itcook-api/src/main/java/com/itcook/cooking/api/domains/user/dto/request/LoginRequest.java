package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.api.domains.user.service.dto.LoginServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;

@Schema(name = "일반 로그인 요청")
public record LoginRequest(
    @Schema(description = "이메일을 입력해주세요.")
    @NotBlank
    String email,
    @Schema(description = "비밀번호를 입력해주세요")
    @NotBlank
    String password,
    @Schema(description = "FCM 디바이스 토큰을 보내주세요")
    String deviceToken
) {

    public LoginServiceDto toServiceDto() {
        return LoginServiceDto.builder()
            .email(email)
            .password(password)
            .deviceToken(deviceToken)
            .build();
    }
}
