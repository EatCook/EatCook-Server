package com.itcook.cooking.infra.s3;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class S3Config {

    private final S3ConfigProperties s3ConfigProperties;

    @Bean
    public AmazonS3 amazonS3() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(s3ConfigProperties.getAccessKey(),
            s3ConfigProperties.getSecretKey());
        return AmazonS3ClientBuilder.standard()
            .withCredentials(new AWSStaticCredentialsProvider(credentials))
            .withRegion(Regions.AP_NORTHEAST_2)
            .build();
    }

}
