package com.itcook.cooking.api.domains.security;

import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.domain.enums.UserRole;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OAuth2User implements ProviderUser{

    private Long id;
    private String email;
    private String password;
    private ProviderType providerType;
    private UserRole role;

    public static OAuth2User of(String username, ProviderType providerType) {
        return OAuth2User.builder()
            .email(username)
            .password(UUID.randomUUID().toString())
            .providerType(providerType)
            .role(UserRole.USER)
            .build();
    }

    @Override
    public String getId() {
        return String.valueOf(id);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public ProviderType getProvider() {
        return providerType;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getRoleName()));
    }
}
