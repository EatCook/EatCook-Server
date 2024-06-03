package com.itcook.cooking.api.domains.security.converter;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.security.ProviderUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import org.springframework.stereotype.Component;

@Component
public class KakaoUserConverter implements ProviderUserConverter{

    @Override
    public boolean isSupports(ProviderType providerType) {
        return providerType == ProviderType.KAKAO;
    }

    @Override
    public AuthenticationUser convert(ProviderUser providerUser) {
        return AuthenticationUser.builder()
            .providerUser(providerUser)
            .build();
    }
}
