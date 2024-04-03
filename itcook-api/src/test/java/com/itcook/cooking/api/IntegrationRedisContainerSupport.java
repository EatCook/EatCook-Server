package com.itcook.cooking.api;

import com.itcook.cooking.api.global.security.jwt.config.RedisTestContainers;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@Import(RedisTestContainers.class)
public abstract class IntegrationRedisContainerSupport {

}
