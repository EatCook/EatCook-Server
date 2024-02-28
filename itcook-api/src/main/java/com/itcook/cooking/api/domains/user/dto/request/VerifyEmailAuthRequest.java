package com.itcook.cooking.api.domains.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "이메일 검증")
public class VerifyEmailAuthRequest {

    @Schema(description = "이메일", example = "user@test.com")
    private String email;
    @Schema(description = "인증 코드", example = "441456")
    private String authCode;

}
