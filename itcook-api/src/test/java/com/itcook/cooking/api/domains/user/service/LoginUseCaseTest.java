package com.itcook.cooking.api.domains.user.service;

import static com.itcook.cooking.api.global.consts.ItCookConstants.BEARER;

import com.itcook.cooking.api.IntegrationTestSupport;
import com.itcook.cooking.api.domains.user.service.dto.UserOAuth2Login;
import com.itcook.cooking.api.domains.user.service.dto.response.SocialLoginResponse;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class LoginUseCaseTest extends IntegrationTestSupport {

//    @Autowired
//    private LoginUseCase loginUseCase;
//
//    @Test
//    @DisplayName("소셜 로그인 테스트")
//    void socialLogin() {
//        //given
//        String token = "sPlIX_aQIA6Au-SA_rqgfWiram8uAwB1AAAAAQo9dGgAAAGQdwKLUv8D-j8FVvr5";
//
//        //when
//        SocialLoginResponse response = loginUseCase.socialLogin(UserOAuth2Login.builder()
//            .providerType(ProviderType.KAKAO)
//            .token(BEARER + token)
//            .build());
//
//        //then
//        System.out.println(response.accessToken());
//        System.out.println(response.refreshToken());
//    }
}