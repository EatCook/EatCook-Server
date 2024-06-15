package com.itcook.cooking.domain.domains.user.entity;

import com.itcook.cooking.domain.common.annotation.DomainService;
import com.itcook.cooking.domain.infra.s3.ImageFileExtension;
import com.itcook.cooking.domain.infra.s3.ImageUrlDto;
import com.itcook.cooking.domain.infra.s3.S3PresignedUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@DomainService
@RequiredArgsConstructor
public class UserImageRegisterService {

    private final S3PresignedUrlService s3PresignedUrlService;

    public ImageUrlDto getImageUrlDto(String fileExtension,
        ItCookUser itCookUser) {
        ImageUrlDto imageUrlDto = ImageUrlDto.builder().build();
        if (StringUtils.hasText(fileExtension)) {
            ImageFileExtension imageFileExtension = ImageFileExtension.fromFileExtension(
                fileExtension);
            imageUrlDto = s3PresignedUrlService.forUser(itCookUser.getId(),
                imageFileExtension.getUploadExtension());
            itCookUser.updateProfile(imageUrlDto.getKey());
        }
        return imageUrlDto;
    }
}
