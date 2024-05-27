package com.itcook.cooking.domain.domains.mock;

import com.itcook.cooking.domain.infra.s3.ImageUrlDto;
import com.itcook.cooking.domain.infra.s3.S3PresignedUrlService;

public class MockS3Service implements S3PresignedUrlService {

    @Override
    public ImageUrlDto forPost(Long userId, Long postId, String fileExtension) {
        return null;
    }

    @Override
    public ImageUrlDto forRecipeProcess(Long userId, Long postId, String fileExtension) {
        return null;
    }

    @Override
    public ImageUrlDto forUser(Long userId, String fileExtension) {
        return null;
    }
}
