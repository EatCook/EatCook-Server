package com.itcook.cooking.api.domains.user.service.dto.response;

import static com.itcook.cooking.api.global.consts.ItCookConstants.BEARER;

import com.itcook.cooking.api.global.security.jwt.dto.TokenDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record SocialLoginResponse(
    @Schema(description = "엑세스 토큰")
    String accessToken,
    @Schema(description = "리프레쉬 토큰")
    String refreshToken
) {

    public static SocialLoginResponse of(TokenDto tokenDto) {
        return SocialLoginResponse.builder()
            .accessToken(BEARER + tokenDto.getAccessToken())
            .refreshToken(BEARER + tokenDto.getRefreshToken())
            .build();
    }

}
