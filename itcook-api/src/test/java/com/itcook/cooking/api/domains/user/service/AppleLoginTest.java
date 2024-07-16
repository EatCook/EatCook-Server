package com.itcook.cooking.api.domains.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.itcook.cooking.api.IntegrationTestSupport;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserOAuth2Login;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.domain.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class AppleLoginTest extends IntegrationTestSupport {

    @Autowired
    private LoginUseCase loginUseCase;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("애플 소셜 로그인 성공 시도한다.")
    void socialLoginApple() {
        //given
        String email = "hangs0908@apple.com";
        UserOAuth2Login userOAuth2Login = UserOAuth2Login.builder()
            .email(email)
            .providerType(ProviderType.APPLE)
            .build();

        //when
        loginUseCase.socialLogin(userOAuth2Login);

        //then
        ItCookUser itCookUser = userRepository.findByEmail(email)
            .orElse(null);

        assertThat(itCookUser.getProviderType()).isEqualTo(ProviderType.APPLE);
        assertThat(itCookUser.getEmail()).isEqualTo(email);
        assertThat(itCookUser.getNickName()).isEqualTo("hangs0908");

    }
}
