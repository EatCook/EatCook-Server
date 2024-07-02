package com.itcook.cooking.infra.feign.client;

import com.itcook.cooking.infra.feign.dto.AppleSocialTokenInfoResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "appleApi", url = "https://appleid.apple.com")
public interface AppleApiClient {

    @PostMapping("/auth/token")
    AppleSocialTokenInfoResponse getIdToken(
        @RequestParam("client_id") String clientId,
        @RequestParam("client_secret") String clientSecret,
        @RequestParam("grant_type") String grantType,
        @RequestParam("code") String code
    );
}
