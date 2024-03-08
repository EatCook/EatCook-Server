package com.itcook.cooking.api.domains.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "이메일 검증")
public class VerifyEmailAuthRequest {

    @Schema(description = "이메일", example = "user@test.com")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    private String email;
    @Schema(description = "인증 코드", example = "441456")
    @NotEmpty
    private String authCode;

}
