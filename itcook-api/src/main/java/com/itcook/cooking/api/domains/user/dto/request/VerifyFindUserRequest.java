package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.api.domains.user.service.dto.VerifyEmailServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Schema(name = "계정 찾기 검증 요청")
public record VerifyFindUserRequest(
    @Schema(description = "이메일", example = "user@test.com")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    String email,
    @Schema(description = "인증 코드", example = "441456")
    @NotBlank(message = "인증 코드를 입력해주세요.")
    String authCode
) {

    public VerifyEmailServiceDto toServiceDto() {
        return VerifyEmailServiceDto.builder()
            .authCode(authCode)
            .email(email)
            .build();
    }

}
