package com.itcook.cooking.domain.domains.user.service.dto;

import com.itcook.cooking.domain.infra.s3.ImageUrlDto;
import lombok.Builder;

@Builder
public record AddSignupDto(
    ImageUrlDto imageUrlDto,
    Long userId
) {

    public static AddSignupDto of(ImageUrlDto imageUrlDto, Long userId) {
        return AddSignupDto.builder()
            .imageUrlDto(imageUrlDto)
            .userId(userId)
            .build()
            ;
    }

    public String getImageUrl() {
        return imageUrlDto.getUrl();
    }
}
