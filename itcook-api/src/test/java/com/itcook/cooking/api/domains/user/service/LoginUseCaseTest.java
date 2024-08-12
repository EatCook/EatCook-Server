package com.itcook.cooking.api.domains.user.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

import com.itcook.cooking.api.IntegrationTestSupport;
import com.itcook.cooking.api.domains.user.service.dto.SocialLoginServiceDto;
import com.itcook.cooking.api.global.consts.ItCookConstants;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.infra.oauth.SocialLoginFactory;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserInfo;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserOAuth2Login;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Transactional
class LoginUseCaseTest extends IntegrationTestSupport {

    @Autowired
    private LoginUseCase loginUseCase;

    @MockBean
    private SocialLoginFactory socialLoginFactory;

    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("카카오 소셜 로그인 성공 시도한다.")
    void socialLoginKakao() {
        //given
        UserOAuth2Login userOAuth2Login = UserOAuth2Login.builder()
            .token("kakaoAccessToken")
            .providerType(ProviderType.KAKAO)
            .build();

        given(socialLoginFactory.socialLogin(userOAuth2Login)).willReturn(
            UserInfo.builder()
                .email("hangs0908@kakao.com")
                .nickName("hangjin")
                .build());

        //when
        loginUseCase.socialLogin(SocialLoginServiceDto.builder()
            .token("kakaoAccessToken")
            .providerType(ProviderType.KAKAO)
            .build());

        //then
        ItCookUser itCookUser = userRepository.findByEmail("hangs0908@kakao.com")
            .orElse(null);

        assertThat(itCookUser).isNotNull();
        assertThat(itCookUser.getProviderType()).isEqualTo(ProviderType.KAKAO);
        assertThat(itCookUser.getNickName()).isEqualTo("hangjin");

    }

}