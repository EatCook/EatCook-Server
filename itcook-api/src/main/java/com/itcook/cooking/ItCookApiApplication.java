package com.itcook.cooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class ItCookApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItCookApiApplication.class, args);
    }
}
