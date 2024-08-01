package com.itcook.cooking.domain.domains.user.domain.entity.dto;

import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import lombok.Builder;

@Builder
public record SignupDto(
    String email,
    String password,
    ProviderType providerType,
    String nickName,
    String deviceToken
) {

    public static SignupDto of(String email, String nickName, String password, ProviderType providerType, String deviceToken) {
        return SignupDto.builder()
            .email(email)
            .password(password)
            .nickName(nickName)
            .providerType(providerType)
            .deviceToken(deviceToken)
            .build()
            ;
    }
}
