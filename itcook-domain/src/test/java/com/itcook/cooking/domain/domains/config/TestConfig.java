package com.itcook.cooking.domain.domains.config;

import com.itcook.cooking.domain.domains.mock.MockEmailService;
import com.itcook.cooking.domain.domains.mock.MockRedisService;
import com.itcook.cooking.domain.domains.mock.MockS3Service;
import com.itcook.cooking.domain.domains.infra.email.AuthCodeService;
import com.itcook.cooking.domain.domains.infra.redis.RedisService;
import com.itcook.cooking.domain.domains.infra.s3.S3PresignedUrlService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class TestConfig {

    @Bean
    public RedisService redisService() {
        return new MockRedisService();
    }

    @Bean
    public AuthCodeService authCodeService() {
        return new MockEmailService();
    }

    @Bean
    public S3PresignedUrlService s3PresignedUrlService() {
        return new MockS3Service();
    }

}
