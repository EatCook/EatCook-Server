package com.itcook.cooking.api.global.config;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.security.CommonUser;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.domain.enums.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithItCookUserSecurityContextFactory implements WithSecurityContextFactory<WithItCookMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithItCookMockUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        CommonUser user = CommonUser.builder()
            .id(1L)
            .email("user@test.com")
            .password("cook1234")
            .providerType(ProviderType.COMMON)
            .role(UserRole.USER)
            .build();

        AuthenticationUser authenticationUser = AuthenticationUser.builder()
            .providerUser(user)
            .build();

        UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken.authenticated(
            authenticationUser, null,
            user.getAuthorities());
        context.setAuthentication(authenticated);
        return context;
    }
}
