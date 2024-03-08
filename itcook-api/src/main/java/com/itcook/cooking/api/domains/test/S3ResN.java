package com.itcook.cooking.api.domains.test;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class S3ResN {
    private String bucketName;
    private String objectKey;
}
