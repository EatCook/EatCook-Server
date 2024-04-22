package com.itcook.cooking.api;

import com.itcook.cooking.infra.email.javamail.JavaMailConfig;
import com.itcook.cooking.infra.email.javamail.JavaMailService;
import com.itcook.cooking.infra.redis.event.SearchWordsEventListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationTestSupport {

    @MockBean
    protected SearchWordsEventListener searchWordsEventListener;

    @MockBean
    protected JavaMailService javaMailService;

    @MockBean
    protected JavaMailConfig javaMailConfig;

}
