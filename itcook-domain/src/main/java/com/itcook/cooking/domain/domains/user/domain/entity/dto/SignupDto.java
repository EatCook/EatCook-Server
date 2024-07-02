package com.itcook.cooking.domain.domains.user.domain.entity.dto;

import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import lombok.Builder;

@Builder
public record SignupDto(
    String email,
    String password,
    ProviderType providerType,
    String nickName
) {

    public static SignupDto of(String email, String password, ProviderType providerType) {
        return SignupDto.builder()
            .email(email)
            .password(password)
            .providerType(providerType)
            .build()
            ;
    }

    public static SignupDto of(String email, String nickName,String password, ProviderType providerType) {
        return SignupDto.builder()
            .email(email)
            .password(password)
            .nickName(nickName)
            .providerType(providerType)
            .build()
            ;
    }
}
