package com.itcook.cooking.api.domains.user.service;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import com.itcook.cooking.api.IntegrationTestSupport;
import com.itcook.cooking.api.domains.user.service.dto.SocialLoginServiceDto;
import com.itcook.cooking.api.global.consts.ItCookConstants;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserOAuth2Login;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.domain.enums.UserRole;
import com.itcook.cooking.domain.domains.user.domain.repository.UserRepository;
import java.util.UUID;
import org.junit.jupiter.api.Disabled;
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
    @DisplayName("기존에 없는 유저가 애플 소셜 간편로그인 성공 시도한다.")
    void socialLoginApple() {
        //given
        String email = "hangs0908@apple.com";

        //when
        loginUseCase.socialLogin(SocialLoginServiceDto.builder()
            .email(email)
            .deviceToken("deviceToken")
            .providerType(ProviderType.APPLE)
            .build());

        //then
        ItCookUser itCookUser = userRepository.findByEmail(email)
            .orElse(null);

        assertThat(itCookUser.getProviderType()).isEqualTo(ProviderType.APPLE);
        assertThat(itCookUser.getEmail()).isEqualTo(email);
        assertThat(itCookUser.getNickName()).isEqualTo("hangs0908");
        assertThat(itCookUser.getDeviceToken()).isEqualTo("deviceToken");

    }

    @Test
    @DisplayName("이미 회원가입한 애플 유저가 애플 소셜 간편로그인 성공 시도한다.")
    void socialLoginAppleAlreadyUser() {
        //given
        String email = "hangs0908@apple.com";

        SocialLoginServiceDto serviceDto = SocialLoginServiceDto.builder()
            .email(email)
            .providerType(ProviderType.APPLE)
            .build();
        loginUseCase.socialLogin(serviceDto);

        //when
        loginUseCase.socialLogin(SocialLoginServiceDto.builder()
            .email(email)
            .deviceToken("deviceToken")
            .providerType(ProviderType.APPLE)
            .build());

        //then
        ItCookUser itCookUser = userRepository.findByEmail(email).get();

        assertThat(itCookUser.getProviderType()).isEqualTo(ProviderType.APPLE);
        assertThat(itCookUser.getEmail()).isEqualTo(email);
        assertThat(itCookUser.getNickName()).isEqualTo("hangs0908");
        assertThat(itCookUser.getDeviceToken()).isEqualTo("deviceToken");

    }
}
