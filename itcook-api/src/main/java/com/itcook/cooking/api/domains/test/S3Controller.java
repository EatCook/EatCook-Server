package com.itcook.cooking.api.domains.test;

import com.itcook.cooking.api.domains.user.dto.response.AddUserResponse;
import com.itcook.cooking.infra.s3.ImageUrlDto;
import com.itcook.cooking.infra.s3.S3PresignedUrlService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class S3Controller {
    private final S3PresignedUrlService s3PresignedUrlService;

//    @PostMapping("/upload1")
//    public AddUserResponse upload(@RequestBody S3Res s3Res) {
//
//        ImageUrlDto test = s3PresignedUrlService.(s3Res.getUserId(), s3Res.getFileExtension());
//        return AddUserResponse.builder()
//                .presignedUrl(test.getUrl())
//                .userId(1L)
//                .build()
//                ;
//    }
//
//    @GetMapping("/getPresignedUrl")
//    public ResponseEntity<String> getPresignedUrl(@RequestBody S3ResN s3ResN) {
//        String s = s3PresignedUrlV1Service.generatePresignedUrl(s3ResN.getBucketName(), s3ResN.getObjectKey());
//
//        return ResponseEntity.ok(s);
//    }

}
