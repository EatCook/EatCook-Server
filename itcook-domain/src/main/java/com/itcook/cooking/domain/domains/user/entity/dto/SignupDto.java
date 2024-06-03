package com.itcook.cooking.domain.domains.user.entity.dto;

import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import lombok.Builder;

@Builder
public record SignupDto(
    String email,
    String password,
    ProviderType providerType
) {

    public static SignupDto of(String email, String password, ProviderType providerType) {
        return SignupDto.builder()
            .email(email)
            .password(password)
            .providerType(providerType)
            .build()
            ;
    }
}
