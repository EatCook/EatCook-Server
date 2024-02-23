package com.itcook.cooking.api.domains.security.converter;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.security.ProviderUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;

public interface ProviderUserConverter {

    boolean isSupports(ProviderType providerType);
    AuthenticationUser convert(ProviderUser providerUser);
}
