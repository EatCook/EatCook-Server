package com.itcook.cooking.infra.oauth.apple;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class AppleFeignConfiguration {

    @Bean
    public ErrorDecoder appleErrorDecoder() {
        return new AppleFeignClientErrorDecoder();
    }
}
