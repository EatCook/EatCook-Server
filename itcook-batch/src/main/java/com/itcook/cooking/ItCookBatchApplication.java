package com.itcook.cooking;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@EnableBatchProcessing
@SpringBootApplication
@ConfigurationPropertiesScan
public class ItCookBatchApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItCookBatchApplication.class, args);
    }

}
