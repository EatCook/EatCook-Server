package com.itcook.cooking.api;

import com.itcook.cooking.api.domains.search.handler.SearchWordsEventHandler;
import com.itcook.cooking.domain.infra.fcm.FcmService;
import com.itcook.cooking.domain.infra.redis.RedisService;
import com.itcook.cooking.domain.infra.s3.S3PresignedUrlService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
//@ContextConfiguration
@TestPropertySource(properties = {
    "aws.s3.access-key=test",
    "aws.s3.secret-key=test",
    "aws.s3.bucket=test",
}, locations = "classpath:application.yml")
//@ActiveProfiles("test")
public abstract class IntegrationTestSupport {

    @MockBean
    protected SearchWordsEventHandler searchWordsEventListener;

    @MockBean
    protected RedisService redisService;

    @MockBean
    protected S3PresignedUrlService s3PresignedUrlService;

//    @MockBean
//    protected FcmConfig fcmConfig;

    @MockBean
    protected FcmService fcmService;

}
