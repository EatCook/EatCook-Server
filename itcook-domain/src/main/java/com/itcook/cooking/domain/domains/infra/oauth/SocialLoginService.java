package com.itcook.cooking.domain.domains.infra.oauth;

import com.itcook.cooking.domain.domains.infra.oauth.dto.InfoForLogin;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserInfo;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;

public interface SocialLoginService {

    boolean isSupports(ProviderType providerType);

    UserInfo attemptLogin(InfoForLogin info);

}