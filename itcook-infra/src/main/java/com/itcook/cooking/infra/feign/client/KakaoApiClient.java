package com.itcook.cooking.infra.feign.client;

import com.itcook.cooking.infra.feign.dto.KakaoUserInfo;
import com.itcook.cooking.infra.oauth.kakao.KakaoFeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "kakaoApi", url = "https://kapi.kakao.com", configuration = KakaoFeignConfiguration.class)
public interface KakaoApiClient {

    @GetMapping("/v1/user/access_token_info")
    void getTokenInfo(@RequestHeader("Authorization") String bearerToken);

    @GetMapping("/v2/user/me")
    KakaoUserInfo getUserInfo(@RequestHeader("Authorization") String bearerToken);

}
