package com.itcook.cooking.api.global;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AppRunner implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        ItCookUser user = ItCookUser.builder()
            .email("hangs0908@user.com")
            .nickName("hangjin")
            .userRole(UserRole.USER)
            .providerType(ProviderType.COMMON)
            .password(passwordEncoder.encode("1234"))
            .build();


        ItCookUser admin = ItCookUser.builder()
            .email("hangs0908@admin.com")
            .nickName("hangjin")
            .userRole(UserRole.ADMIN)
            .providerType(ProviderType.COMMON)
            .password(passwordEncoder.encode("1234"))
            .build();

        userRepository.saveAll(List.of(user,admin));
    }
}
