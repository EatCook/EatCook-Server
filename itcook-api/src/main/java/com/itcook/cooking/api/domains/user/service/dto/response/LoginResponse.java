package com.itcook.cooking.api.domains.user.service.dto.response;

import static com.itcook.cooking.api.global.consts.ItCookConstants.BEARER;

import com.itcook.cooking.api.global.security.jwt.dto.TokenDto;
import lombok.Builder;

@Builder
public record LoginResponse(
    String accessToken,
    String refreshToken
) {

    public static LoginResponse of(TokenDto tokenDto) {
        return LoginResponse.builder()
            .accessToken(BEARER + tokenDto.getAccessToken())
            .refreshToken(BEARER + tokenDto.getRefreshToken())
            .build();
    }
}
