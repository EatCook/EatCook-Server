package com.itcook.cooking.api.domains.user.service.dto;

import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import lombok.Builder;

@Builder
public record LoginServiceDto(
    String email,
    String password,
    String deviceToken
) {
    public ItCookUser toEntity() {
        return ItCookUser.builder()
            .email(email)
            .password(password)
            .deviceToken(deviceToken)
            .build();
    }
}
