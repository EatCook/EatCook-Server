package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.api.domains.user.service.dto.UserOAuth2Login;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Pattern;

@Schema(name = "소셜 로그인 요청")
public record SocialLoginRequest(
//    String email,
    @Schema(description = "소셜 로그인 타입", example = "APPLE or KAKAO")
    ProviderType providerType,
//    @Pattern(regexp = "^Bearer .*$", message = "Bearer 로 시작하는 토큰값을 보내주세요.")
    @Schema(description = "카카오일 경우 access token (Bearer 포함한), 애플일 경우 인가 코드", example = "afsdfsdasdf")
    String token
) {

    public UserOAuth2Login of() {
        return UserOAuth2Login.builder()
//            .email(email)
            .providerType(providerType)
            .token(token)
            .build();
    }
}
