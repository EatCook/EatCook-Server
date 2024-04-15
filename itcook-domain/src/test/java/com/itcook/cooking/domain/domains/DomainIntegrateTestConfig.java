package com.itcook.cooking.domain.domains;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(
    basePackages = "com.itcook"
)
public class DomainIntegrateTestConfig {

}
