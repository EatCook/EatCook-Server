package com.itcook.cooking.api.domains.user.service.dto;

import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import lombok.Builder;

@Builder
public record MyPageProfileImageDto(
    String email,
    String fileExtension
) {
    public ItCookUser toEntity() {
        return ItCookUser.builder()
            .email(email)
            .build();
    }
}
