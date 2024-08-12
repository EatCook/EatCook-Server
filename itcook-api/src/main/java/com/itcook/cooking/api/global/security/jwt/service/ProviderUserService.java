package com.itcook.cooking.api.global.security.jwt.service;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.security.ProviderUser;
import com.itcook.cooking.api.domains.security.converter.ProviderUserConverter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProviderUserService {

    private final List<ProviderUserConverter> providerUserConverters;


    public AuthenticationUser convert(ProviderUser providerUser) {
        for (ProviderUserConverter providerUserConverter : providerUserConverters) {
            if (providerUserConverter.isSupports(providerUser.getProvider())) {
                return providerUserConverter.convert(providerUser);
            }
        }
        return null;
    }

}
