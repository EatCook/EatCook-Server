package com.itcook.cooking.api.domains.user.service.dto;

import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import lombok.Builder;

@Builder
public record CheckNickNameServiceDto(
    String nickName
) {
    public ItCookUser toEntity() {
        return ItCookUser.builder()
            .nickName(nickName)
            .build()
            ;
    }
}
