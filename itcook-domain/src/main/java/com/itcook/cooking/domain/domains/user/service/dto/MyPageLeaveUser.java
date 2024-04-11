package com.itcook.cooking.domain.domains.user.service.dto;

import lombok.Builder;

@Builder
public record MyPageLeaveUser(
    String email
) {

    public static MyPageLeaveUser of(String email) {
        return MyPageLeaveUser.builder()
            .email(email)
            .build();
    }
}
