package com.itcook.cooking.infra.oauth.kakao;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.infra.oauth.SocialLoginService;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserInfo;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.infra.feign.client.KakaoApiClient;
import com.itcook.cooking.infra.feign.dto.KakaoUserInfo;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoLoginService implements SocialLoginService {

    private final KakaoApiClient kakaoApiClient;

    @Override
    public boolean isSupports(ProviderType providerType) {
        return providerType == ProviderType.KAKAO;
    }

    @Override
    public UserInfo attemptLogin(String token) {
        try{
            kakaoApiClient.getTokenInfo(token);
            KakaoUserInfo kakaoUserInfo = kakaoApiClient.getUserInfo(token);
            return kakaoUserInfo.of();
        } catch (FeignException e) {
            log.error("카카오 로그인 에러 : {}", e.getMessage());
            throw new ApiException(UserErrorCode.TOKEN_NOT_VALID);
        }
    }

}
