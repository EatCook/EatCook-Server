package com.itcook.cooking.domain.domains.user.entity.dto;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
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
