package com.itcook.cooking.api;

import com.itcook.cooking.api.global.config.SwaggerConfig;
import com.itcook.cooking.infra.email.javamail.JavaMailConfig;
import com.itcook.cooking.infra.email.javamail.JavaMailService;
import com.itcook.cooking.infra.redis.RedisService;
import com.itcook.cooking.infra.redis.event.SearchWordsEventListener;
import com.itcook.cooking.infra.s3.S3PresignedUrlService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationTestSupport {

    @MockBean
    protected SearchWordsEventListener searchWordsEventListener;

    @MockBean
    protected RedisService redisService;

    @MockBean
    protected S3PresignedUrlService s3PresignedUrlService;

    @MockBean
    protected SwaggerConfig swaggerConfig;


}
