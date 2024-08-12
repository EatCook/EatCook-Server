package com.itcook.cooking.domain.domains.infra.oauth;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserInfo;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserOAuth2Login;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialLoginFactory {

    private final List<SocialLoginService> socialLoginServices;

    public UserInfo socialLogin(UserOAuth2Login userOAuth2Login) {
        return socialLoginServices.stream()
            .filter(
                socialLoginService -> socialLoginService.isSupports(userOAuth2Login.providerType()))
            .findFirst()
            .orElseThrow(() -> new ApiException(UserErrorCode.NOT_EQUAL_PROVIDER_TYPE))
            .attemptLogin(userOAuth2Login.of());
    }

}
