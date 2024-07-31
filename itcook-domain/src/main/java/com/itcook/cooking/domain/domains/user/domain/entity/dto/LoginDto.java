package com.itcook.cooking.domain.domains.user.domain.entity.dto;

import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import lombok.Builder;

@Builder
public record LoginDto(
    String password,
    String deviceToken
) {
    public static LoginDto of(ItCookUser user) {
        return LoginDto.builder()
            .password(user.getPassword())
            .deviceToken(user.getDeviceToken())
            .build();
    }
}
