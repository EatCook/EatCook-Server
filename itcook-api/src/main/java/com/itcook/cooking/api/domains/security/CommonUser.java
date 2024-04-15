package com.itcook.cooking.api.domains.security;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import java.util.List;
import java.util.Map;
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
public class CommonUser implements ProviderUser {

    private Long id;
    private String email;
    private String password;
    private ProviderType providerType;
    private UserRole role;

    public static CommonUser of(ItCookUser itCookUser) {
        return CommonUser.builder()
            .id(itCookUser.getId())
            .email(itCookUser.getEmail())
            .password(itCookUser.getPassword())
            .providerType(ProviderType.COMMON)
            .role(UserRole.USER)
            .build();

    }

    public static CommonUser of(String username) {
        return CommonUser.builder()
            .email(username)
            .password("")
            .providerType(ProviderType.COMMON)
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
        return ProviderType.COMMON;
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
