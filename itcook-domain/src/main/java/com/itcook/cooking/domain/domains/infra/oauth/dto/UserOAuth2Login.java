package com.itcook.cooking.domain.domains.infra.oauth.dto;

import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import lombok.Builder;

@Builder
public record UserOAuth2Login(
    String email,
    ProviderType providerType,
    String token,
    String deviceToken
) {

    public InfoForLogin of() {
        return InfoForLogin.builder()
            .email(email)
            .token(token)
            .build();
    }

}
