package com.itcook.cooking.infra.oauth.kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcook.cooking.infra.oauth.apple.AppleFeignClientErrorDecoder;
import feign.codec.ErrorDecoder;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class KakaoFeignConfiguration {

    @Bean
    public ErrorDecoder kakaoErrorDecoder() {
        return new KakaoFeignClientErrorDecoder();
    }
}
