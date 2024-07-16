package com.itcook.cooking.api.domains.user.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.itcook.cooking.api.IntegrationTestSupport;
import com.itcook.cooking.api.global.consts.ItCookConstants;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserOAuth2Login;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LoginUseCaseTest extends IntegrationTestSupport {

    @Autowired
    private LoginUseCase loginUseCase;

    @Test
    @DisplayName("소셜 로그인 테스트")
    void socialLoginTest() {
        //given
        String token = "sPlIX_aQIA6Au-SA_rqgfWiram8uAwB1AAAAAQo9dGgAAAGQdwKLUv8D-j8FVvr5";

        //when
        assertThatThrownBy(() -> loginUseCase.socialLogin(UserOAuth2Login.builder()
            .providerType(ProviderType.KAKAO)
            .token(ItCookConstants.BEARER + token)
            .build()))
        .isInstanceOf(ApiException.class)
            ;
    }
}