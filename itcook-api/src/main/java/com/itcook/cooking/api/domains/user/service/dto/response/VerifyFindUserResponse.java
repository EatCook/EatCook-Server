package com.itcook.cooking.api.domains.user.service.dto.response;

import lombok.Builder;

@Builder
public record VerifyFindUserResponse(
    String email,
    String password
) {

    public static VerifyFindUserResponse of(String email, String password) {
        return VerifyFindUserResponse.builder()
            .email(email)
            .password(password)
            .build()
            ;

    }
}
