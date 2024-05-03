package com.itcook.cooking.domain.domains.user.service;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.infra.s3.ImageFileExtension;
import com.itcook.cooking.infra.s3.ImageUrlDto;
import com.itcook.cooking.infra.s3.S3PresignedUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
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
