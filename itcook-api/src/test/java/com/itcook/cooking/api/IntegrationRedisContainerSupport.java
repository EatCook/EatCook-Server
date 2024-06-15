package com.itcook.cooking.api;

import com.itcook.cooking.api.global.security.jwt.config.RedisTestContainers;
import com.itcook.cooking.domain.infra.email.AuthCodeService;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@Import(RedisTestContainers.class)
public abstract class IntegrationRedisContainerSupport {

    @MockBean
    protected AuthCodeService javaMailService;

//    @MockBean
//    protected JavaMailConfig javaMailConfig;


}
