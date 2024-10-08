package com.itcook.cooking.infra.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;

import com.itcook.cooking.domain.domains.infra.s3.ImageUrlDto;
import com.itcook.cooking.domain.domains.infra.s3.S3PresignedUrlService;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3PresignedUrlServiceImpl implements S3PresignedUrlService {

    private final AmazonS3 amazonS3;
    @Value("${aws.s3.bucket}")
    private String bucket;
    private String baseUrl = "image";

    public ImageUrlDto forPost(Long userId, Long postId, String fileExtension) {
        String fileName = getForPostImageFileName(userId, postId, fileExtension);
        log.info("fileName: {}", fileName);
        URL url = amazonS3
                .generatePresignedUrl(getGeneratePresignedUrlRequest(bucket, fileName, fileExtension));
        return ImageUrlDto.of(url.toString(), fileName);
    }

    private String getForPostImageFileName(Long userId, Long postId, String fileExtension) {
        return baseUrl
                + "/post/"
                + userId.toString()
                + "/"
                + postId.toString()
                + "/"
                + UUID.randomUUID()
                + "."
                + fileExtension;
    }

    public ImageUrlDto forRecipeProcess(Long userId, Long postId, String fileExtension) {
        String fileName = getForRecipeProcessImageFileName(userId, postId, fileExtension);
        log.info("fileName: {}", fileName);
        URL url = amazonS3
                .generatePresignedUrl(getGeneratePresignedUrlRequest(bucket, fileName, fileExtension));
        return ImageUrlDto.of(url.toString(), fileName);
    }

    private String getForRecipeProcessImageFileName(Long userId, Long postId, String fileExtension) {
        return baseUrl
                + "/post/"
                + userId.toString()
                + "/"
                + postId.toString()
                + "/"
                + "recipeprcess"
                + "/"
                + UUID.randomUUID()
                + "."
                + fileExtension;
    }

//    public ImageFileExtension fileExtensionValidation(String fileExtension) {
//        if (fileExtension.equalsIgnoreCase("jpg")) {
//            return ImageFileExtension.JPG;
//        } else if (fileExtension.equalsIgnoreCase("jpeg")) {
//            return ImageFileExtension.JPEG;
//        } else if (fileExtension.equalsIgnoreCase("png")) {
//            return ImageFileExtension.PNG;
//        } else {
//            //허용되지 않은 확장자명
//            return null;
//        }
//    }

    public ImageUrlDto forUser(Long userId, String fileExtension) {
//        String fileExtension = imageFileExtension.getUploadExtension();
        String fileName = getForUserFileName(userId, fileExtension);
        log.info("fileName: {}", fileName);
        URL url = amazonS3
                .generatePresignedUrl(getGeneratePresignedUrlRequest(bucket, fileName, fileExtension));
        return ImageUrlDto.of(url.toString(), fileName);
    }

    private String getForUserFileName(Long userId, String fileExtension) {
        return baseUrl
                + "/user/"
                + userId.toString()
                + "/"
                + UUID.randomUUID()
                + "."
                + fileExtension;
    }


    private GeneratePresignedUrlRequest getGeneratePresignedUrlRequest(
            String bucket, String fileName, String fileExtension
    ) {
        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(
                bucket, fileName)
                .withMethod(HttpMethod.PUT)
                .withKey(fileName)
                .withContentType("image/" + fileExtension)
                .withExpiration(getExpirationDate());
        generatePresignedUrlRequest.addRequestParameter(
                Headers.S3_CANNED_ACL, CannedAccessControlList.PublicRead.toString()
        );
        return generatePresignedUrlRequest;
    }

    private Date getExpirationDate() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        // 3분
        expTimeMillis += 1000 * 60 * 3;
        expiration.setTime(expTimeMillis);
        return expiration;
    }


}
