package com.itcook.cooking.domain.domains;

import com.itcook.cooking.domain.domains.config.TestConfig;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

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
