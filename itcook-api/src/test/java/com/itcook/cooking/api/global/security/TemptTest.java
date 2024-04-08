package com.itcook.cooking.api.global.security;


import com.itcook.cooking.api.IntegrationTestSupport;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

public class TemptTest extends IntegrationTestSupport {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("tempt")
    void tempt1() {
        //given
        String password = "cook1234";
        ItCookUser user = ItCookUser.builder()
            .email("cook1234@test.com")
            .password(passwordEncoder.encode(password))
            .nickName("잇쿡1")
            .userRole(UserRole.USER)
            .providerType(ProviderType.COMMON)
            .build();

        userRepository.save(user);

        //when
        ItCookUser itCookUser = userRepository.findById(user.getId()).get();
        System.out.println("itCookUser.getPassword() = " + itCookUser.getPassword());
        String verifyPassword = passwordEncoder.encode("cook1234");
        System.out.println("verifyPassword = " + verifyPassword);
        System.out.println("-------------------------------");
        if (passwordEncoder.matches("cook1234", itCookUser.getPassword())) {
            System.out.println("일치합니다.");
        }

        //then
    }

}
