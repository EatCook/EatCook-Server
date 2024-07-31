package com.itcook.cooking.api.domains.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.itcook.cooking.api.IntegrationRedisContainerSupport;
import com.itcook.cooking.api.domains.user.service.dto.LoginServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.response.LoginResponse;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.domain.enums.UserRole;
import com.itcook.cooking.domain.domains.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class CommonLoginUseCaseTest extends IntegrationRedisContainerSupport {

    @Autowired
    LoginUseCase loginUseCase;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("이메일, 패스워드, 디바이스 토큰을 받아 로그인을 시도한다")
    void login() {
        //given
        String email = "user@test.com";
        String nickName = "잇쿡";
        ItCookUser user = createUser(email, nickName);
        LoginServiceDto serviceDto = LoginServiceDto.builder()
            .email(email)
            .password("cook12345")
            .deviceToken("deviceToken")
            .build();

        //when
        var response = loginUseCase.login(serviceDto);

        //then
        ItCookUser findUser = userRepository.findById(user.getId()).get();
        assertThat(response.accessToken()).isNotNull();
        assertThat(response.refreshToken()).isNotNull();
        assertThat(findUser.getNickName()).isEqualTo(nickName);
        assertThat(findUser.getEmail()).isEqualTo(email);
        assertThat(findUser.getDeviceToken()).isEqualTo("deviceToken");
    }

    private ItCookUser createUser(String username, String nickName) {
        ItCookUser user = ItCookUser.builder()
            .email(username)
            .password(passwordEncoder.encode("cook12345"))
            .providerType(ProviderType.COMMON)
            .nickName(nickName)
            .userRole(UserRole.USER)
            .build();

        return userRepository.save(user);
    }

}
