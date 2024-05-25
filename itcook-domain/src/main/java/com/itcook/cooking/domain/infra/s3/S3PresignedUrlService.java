package com.itcook.cooking.domain.infra.s3;

public interface S3PresignedUrlService {

    ImageUrlDto forPost(Long userId, Long postId, String fileExtension);

    ImageUrlDto forRecipeProcess(Long userId, Long postId, String fileExtension);

    ImageUrlDto forUser(Long userId, String fileExtension);

}
