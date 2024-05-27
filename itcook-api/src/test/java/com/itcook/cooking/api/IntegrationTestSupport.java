package com.itcook.cooking.api;

import com.itcook.cooking.api.domains.search.handler.SearchWordsEventHandler;
import com.itcook.cooking.domain.infra.redis.RedisService;
import com.itcook.cooking.domain.infra.s3.S3PresignedUrlService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationTestSupport {

    @MockBean
    protected SearchWordsEventHandler searchWordsEventListener;

    @MockBean
    protected RedisService redisService;

    @MockBean
    protected S3PresignedUrlService s3PresignedUrlService;

}
