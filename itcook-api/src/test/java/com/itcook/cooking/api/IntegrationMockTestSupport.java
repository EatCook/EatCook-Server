package com.itcook.cooking.api;

import com.itcook.cooking.infra.redis.event.SearchWordsEventListener;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public abstract class IntegrationMockTestSupport {

    @MockBean
    private SearchWordsEventListener searchWordsEventListener;

}
