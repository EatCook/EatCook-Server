package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.api.domains.user.service.dto.VerifyEmailServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "이메일 검증 요청")
public class VerifyEmailAuthRequest {

    @Schema(description = "이메일", example = "user@test.com")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @Schema(description = "인증 코드", example = "441456")
    @NotBlank(message = "인증 코드를 입력해주세요.")
    private String authCode;

    public VerifyEmailServiceDto toServiceDto() {
        return VerifyEmailServiceDto.builder()
            .authCode(authCode)
            .email(email)
            .build();
    }

}
