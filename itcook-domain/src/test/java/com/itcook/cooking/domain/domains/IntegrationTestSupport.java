package com.itcook.cooking.domain.domains;

import com.itcook.cooking.DomainTestApplication;
import com.itcook.cooking.domain.domains.config.TestConfig;
import com.itcook.cooking.domain.infra.fcm.FcmService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
//@ContextConfiguration(classes = DomainTestApplication.class)
//@TestPropertySource("classpath:application-domain-test.yml")
@Import({TestConfig.class})
@ActiveProfiles(value = "domain-test")
public abstract class IntegrationTestSupport {
//
//    @MockBean
//    public FcmService fcmService;

}
