package com.itcook.cooking.infra.oauth.apple;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.infra.oauth.SocialLoginService;
import com.itcook.cooking.domain.domains.infra.oauth.dto.InfoForLogin;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserInfo;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.infra.feign.client.AppleApiClient;
import com.itcook.cooking.infra.oauth.utils.AppleJwtUtils;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppleLoginService implements SocialLoginService {

    private final AppleApiClient appleApiClient;
    private final AppleJwtUtils appleJwtUtils;
    private final AppleProperties appleProperties;


    @Override
    public boolean isSupports(ProviderType providerType) {
        return providerType == ProviderType.APPLE;
    }

    /**
     String idToken = appleApiClient.getIdToken(
     appleProperties.getClientId(),
     appleJwtUtils.generateClientSecret(),
     appleProperties.getGrantType(),
     token // authorization code
     ).getIdToken();

     return appleJwtUtils.decodePayload(idToken,
     AppleIdTokenPayload.class).of();
     *
     */
    @Override
    public UserInfo attemptLogin(InfoForLogin info) {
        String email = info.email();
        String nickName = email.split("@")[0];
        return UserInfo.builder()
            .email(email)
            .nickName(nickName)
            .build();
    }


}
