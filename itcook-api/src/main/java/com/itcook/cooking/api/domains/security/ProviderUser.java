package com.itcook.cooking.api.domains.security;

import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;

public interface ProviderUser {
    String getId();
    String getUsername();
    String getEmail();
    String getPassword();
    ProviderType getProvider();
    Map<String, Object> getAttributes();
    List<? extends GrantedAuthority> getAuthorities();
}
