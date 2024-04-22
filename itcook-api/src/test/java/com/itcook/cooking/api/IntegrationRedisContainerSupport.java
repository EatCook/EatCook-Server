package com.itcook.cooking.api;

import com.itcook.cooking.api.global.security.jwt.config.RedisTestContainers;
import com.itcook.cooking.infra.email.javamail.JavaMailConfig;
import com.itcook.cooking.infra.email.javamail.JavaMailService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@Import(RedisTestContainers.class)
public abstract class IntegrationRedisContainerSupport {

    @MockBean
    protected JavaMailService javaMailService;

    @MockBean
    protected JavaMailConfig javaMailConfig;


}
