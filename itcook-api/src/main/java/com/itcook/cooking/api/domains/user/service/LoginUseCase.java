package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.domain.domains.infra.oauth.dto.UserOAuth2Login;
import com.itcook.cooking.api.domains.user.service.dto.response.SocialLoginResponse;
import com.itcook.cooking.api.global.security.jwt.service.JwtTokenProvider;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.infra.oauth.SocialLoginFactory;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserInfo;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.entity.dto.SignupDto;
import com.itcook.cooking.domain.domains.user.domain.entity.validator.UserValidator;
import com.itcook.cooking.domain.domains.user.domain.enums.UserRole;
import com.itcook.cooking.domain.domains.user.domain.repository.UserRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final JwtTokenProvider jwtTokenProvider;
    private final SocialLoginFactory socialLoginFactory;

    @Transactional
    public SocialLoginResponse socialLogin(UserOAuth2Login userOAuth2Login) {
        UserInfo userInfo = socialLoginFactory.socialLogin(userOAuth2Login);

        ItCookUser user = signup(userOAuth2Login, userInfo);
        changeFcmToken(userOAuth2Login, user);

        return SocialLoginResponse.of(
            jwtTokenProvider.generateAccessTokenAndRefreshToken(userInfo.getEmail(),
                List.of("ROLE_" + UserRole.USER.getRoleName())));
    }

    private ItCookUser signup(UserOAuth2Login userOAuth2Login, UserInfo userInfo) {
        return userRepository.findByEmail(userInfo.getEmail())
            .orElseGet(() -> userRepository.save(
                ItCookUser.signup(SignupDto.of(userInfo.getEmail(), userInfo.getNickName(),
                    UUID.randomUUID().toString(), userOAuth2Login.providerType()), userValidator)
            ));
    }

    private void changeFcmToken(UserOAuth2Login userOAuth2Login, ItCookUser user) {
        user.changeDeviceToken(userOAuth2Login.deviceToken());
    }

}
