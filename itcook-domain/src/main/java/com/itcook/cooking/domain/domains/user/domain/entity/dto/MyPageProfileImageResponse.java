package com.itcook.cooking.domain.domains.user.domain.entity.dto;

import lombok.Builder;

@Builder
public record MyPageProfileImageResponse(
    String presignedUrl
) {

    public static MyPageProfileImageResponse from(String presignedUrl) {
        return MyPageProfileImageResponse.builder()
            .presignedUrl(presignedUrl)
            .build();
    }
}
