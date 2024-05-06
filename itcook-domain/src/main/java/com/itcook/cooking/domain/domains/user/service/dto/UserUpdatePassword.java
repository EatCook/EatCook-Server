package com.itcook.cooking.domain.domains.user.service.dto;

import lombok.Builder;

@Builder
public record UserUpdatePassword(
    String email,
    String rawCurrentPassword,
    String newPassword
) {

}
