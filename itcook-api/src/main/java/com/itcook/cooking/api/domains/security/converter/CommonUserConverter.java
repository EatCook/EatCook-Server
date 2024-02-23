package com.itcook.cooking.api.domains.security.converter;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.security.ProviderUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import org.springframework.stereotype.Service;

@Service
public class CommonUserConverter implements ProviderUserConverter{


    @Override
    public boolean isSupports(ProviderType providerType) {
        return providerType == ProviderType.COMMON;
    }

    @Override
    public AuthenticationUser convert(ProviderUser providerUser) {
        return AuthenticationUser.builder()
            .providerUser(providerUser)
            .build();
    }
}
