package com.itcook.cooking.domain.domains.user.domain.entity.dto;

import lombok.Builder;

@Builder
public record AddSignupDomainResponse(
    String presignedUrl
) {

    public static AddSignupDomainResponse of(String presignedUrl) {
        return AddSignupDomainResponse.builder()
            .presignedUrl(presignedUrl)
            .build()
            ;
    }
}
