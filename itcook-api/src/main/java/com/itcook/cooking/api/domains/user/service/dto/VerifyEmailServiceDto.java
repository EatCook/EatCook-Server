package com.itcook.cooking.api.domains.user.service.dto;

import lombok.Builder;

@Builder
public record VerifyEmailServiceDto(
    String email,
    String authCode
) {

}
