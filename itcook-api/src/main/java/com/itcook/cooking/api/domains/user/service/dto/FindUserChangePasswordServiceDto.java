package com.itcook.cooking.api.domains.user.service.dto;

import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import lombok.Builder;

@Builder
public record FindUserChangePasswordServiceDto(
    String email,
    String password
) {

    public ItCookUser toEntity() {
        return ItCookUser.builder()
            .email(email)
            .password(password)
            .build();
    }
}
