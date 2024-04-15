package com.itcook.cooking.domain.domains;

import com.itcook.cooking.DomainTestApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
//@ContextConfiguration(classes = DomainTestApplication.class)
//@TestPropertySource("classpath:application-domain-test.yml")
@ActiveProfiles(value = "domain-test")
public abstract class IntegrationTestSupport {

}
