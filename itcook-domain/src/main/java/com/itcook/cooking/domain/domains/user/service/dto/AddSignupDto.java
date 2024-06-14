package com.itcook.cooking.domain.domains.user.service.dto;

import lombok.Builder;

@Builder
public record AddSignupDto(
    String presignedUrl
) {

    public static AddSignupDto of(String presignedUrl) {
        return AddSignupDto.builder()
            .presignedUrl(presignedUrl)
            .build()
            ;
    }

}
