package com.itcook.cooking.domain.domains.infra.oauth;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserInfo;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SocialLoginFactory {

    private final List<SocialLoginService> socialLoginServices;

    public UserInfo socialLogin(ProviderType providerType, String token) {
        return socialLoginServices.stream()
            .filter(
                socialLoginService -> socialLoginService.isSupports(providerType))
            .findFirst()
            .orElseThrow(() -> new ApiException(UserErrorCode.NOT_EQUAL_PROVIDER_TYPE))
            .attemptLogin(token);
    }

}
