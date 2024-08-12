package com.itcook.cooking.api;

import com.itcook.cooking.api.global.security.jwt.config.RedisTestContainers;
import com.itcook.cooking.domain.domains.infra.email.AuthCodeService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "aws.s3.access-key=test",
    "aws.s3.secret-key=test",
    "aws.s3.bucket=test",
}, locations = "classpath:application.yml")
@Import(RedisTestContainers.class)
public abstract class IntegrationRedisContainerSupport {

    @MockBean
    protected AuthCodeService javaMailService;


}
