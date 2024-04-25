package com.itcook.cooking.infra.s3;

public interface ImageService {

    ImageUrlDto forPost(Long userId, Long postId, String fileExtension) ;
    ImageUrlDto forRecipeProcess(Long userId, Long postId, String fileExtension) ;
    ImageUrlDto forUser(Long userId, String fileExtension);


}
