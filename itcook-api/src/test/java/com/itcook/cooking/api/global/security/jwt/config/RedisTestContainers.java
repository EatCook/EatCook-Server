package com.itcook.cooking.api.global.security.jwt.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class RedisTestContainers {

    static {

        var redis = new GenericContainer<>(DockerImageName.parse("redis:alpine"))
            .withExposedPorts(6379)
            .withReuse(true)
            ;

        redis.start();

        System.setProperty("spring.redis.host", redis.getHost());
        System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
    }

}
