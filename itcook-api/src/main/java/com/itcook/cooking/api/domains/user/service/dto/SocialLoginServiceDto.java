package com.itcook.cooking.api.domains.user.service.dto;

import com.itcook.cooking.domain.domains.infra.oauth.dto.UserInfo;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserOAuth2Login;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import java.util.UUID;
import lombok.Builder;

@Builder
public record SocialLoginServiceDto(
    String email,
    ProviderType providerType,
    String token,
    String deviceToken
) {

    public UserOAuth2Login toOAuth2Login() {
        return UserOAuth2Login.builder()
            .providerType(providerType)
            .email(email)
            .token(token)
            .deviceToken(deviceToken)
            .build();
    }

    public ItCookUser toEntity(UserInfo userInfo) {
        return ItCookUser.builder()
            .email(userInfo.getEmail())
            .nickName(userInfo.getNickName())
            .password(UUID.randomUUID().toString())
            .deviceToken(deviceToken)
            .providerType(providerType)
            .build();
    }
}
