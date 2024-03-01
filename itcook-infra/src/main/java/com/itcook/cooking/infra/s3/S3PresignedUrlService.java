package com.itcook.cooking.infra.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.Headers;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
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
public class S3PresignedUrlService {

    private final AmazonS3 amazonS3;
    @Value("${aws.s3.bucket}")
    private String bucket;
    private String baseUrl = "image";

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
