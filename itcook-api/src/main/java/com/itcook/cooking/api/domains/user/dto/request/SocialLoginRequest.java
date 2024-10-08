package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.api.domains.user.service.dto.SocialLoginServiceDto;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserOAuth2Login;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotNull;

@Schema(name = "소셜 로그인 요청")
public record SocialLoginRequest(
    @Schema(description = "소셜 로그인 타입", example = "APPLE or KAKAO")
    @NotNull
    ProviderType providerType,
    @Schema(description = "카카오일 경우 access token (Bearer 포함한)")
    String token,
    @Schema(description = "애플일 경우에만 애플 아이디를 보내주세요. ")
    String email,
    @Schema(description = "fcm 디바이스 토큰을 보내주세요.")
    String deviceToken
) {

    public UserOAuth2Login of() {
        return UserOAuth2Login.builder()
            .providerType(providerType)
            .email(email)
            .token(token)
            .deviceToken(deviceToken)
            .build();
    }

    public SocialLoginServiceDto toServiceDto() {
        return SocialLoginServiceDto.builder()
            .providerType(providerType)
            .email(email)
            .token(token)
            .deviceToken(deviceToken)
            .build();
    }
}
