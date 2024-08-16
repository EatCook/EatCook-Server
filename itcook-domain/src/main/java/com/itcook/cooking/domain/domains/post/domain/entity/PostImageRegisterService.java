package com.itcook.cooking.domain.domains.post.domain.entity;

import com.itcook.cooking.domain.common.annotation.DomainService;
import com.itcook.cooking.domain.domains.infra.s3.ImageFileExtension;
import com.itcook.cooking.domain.domains.infra.s3.ImageUrlDto;
import com.itcook.cooking.domain.domains.infra.s3.S3PresignedUrlService;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@DomainService
@RequiredArgsConstructor
public class PostImageRegisterService {

    private final S3PresignedUrlService s3PresignedUrlService;

    public ImageUrlDto getPostImageUrlDto(Long userId, Long postId, String fileExtension) {
        ImageUrlDto imageUrlDto = ImageUrlDto.builder().build();
        if (StringUtils.hasText(fileExtension)) {
            ImageFileExtension imageFileExtension = ImageFileExtension.fromFileExtension(
                    fileExtension);
            imageUrlDto = s3PresignedUrlService.forPost(userId, postId,
                    imageFileExtension.getUploadExtension());
        }
        return imageUrlDto;
    }

    public ImageUrlDto getRecipeImageUrlDto(Long userId, Long postId, String fileExtension) {
        ImageUrlDto imageUrlDto = ImageUrlDto.builder().build();
        if (StringUtils.hasText(fileExtension)) {
            ImageFileExtension imageFileExtension = ImageFileExtension.fromFileExtension(
                    fileExtension);
            imageUrlDto = s3PresignedUrlService.forRecipeProcess(userId, postId,
                    imageFileExtension.getUploadExtension());
        }
        return imageUrlDto;
    }
}
