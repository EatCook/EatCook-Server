package com.itcook.cooking.infra.s3;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "aws.s3")
@ConstructorBinding
public class S3ConfigProperties {

    private final String accessKey;
    private final String secretKey;

}
