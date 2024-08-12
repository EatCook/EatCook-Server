package com.itcook.cooking.api.domains.user.service.dto;

import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import lombok.Builder;

@Builder
public record SignupServiceDto(
    String email,
    String password
) {

    public ItCookUser toEntity() {
        return ItCookUser.builder()
            .email(email)
            .password(password)
            .providerType(ProviderType.COMMON)
            .build()
            ;
    }
}
