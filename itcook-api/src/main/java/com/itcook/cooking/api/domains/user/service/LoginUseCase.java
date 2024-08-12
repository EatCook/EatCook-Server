package com.itcook.cooking.api.domains.user.service;

import com.itcook.cooking.api.domains.user.service.dto.LoginServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.SocialLoginServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.response.LoginResponse;
import com.itcook.cooking.api.domains.user.service.dto.response.SocialLoginResponse;
import com.itcook.cooking.api.global.security.jwt.service.JwtTokenProvider;
import com.itcook.cooking.domain.common.annotation.UseCase;
import com.itcook.cooking.domain.domains.infra.oauth.SocialLoginFactory;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserInfo;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.UserRole;
import com.itcook.cooking.domain.domains.user.domain.repository.UserRepository;
import com.itcook.cooking.domain.domains.user.service.UserService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final SocialLoginFactory socialLoginFactory;
    private final UserService userService;

    @Transactional
    public SocialLoginResponse socialLogin(SocialLoginServiceDto serviceDto) {
        UserInfo userInfo = socialLoginFactory.socialLogin(serviceDto.toOAuth2Login());

        signup(serviceDto.toEntity(userInfo));

        return SocialLoginResponse.of(
            jwtTokenProvider.generateAccessTokenAndRefreshToken(userInfo.getEmail(),
                List.of("ROLE_" + UserRole.USER.getRoleName())));
    }

    private void signup(ItCookUser entity) {
        ItCookUser user = userRepository.findByEmail(entity.getEmail())
            .orElseGet(() -> userService.signup(entity));
        user.changeDeviceToken(entity.getDeviceToken());
    }

    public LoginResponse login(LoginServiceDto loginServiceDto) {
        userService.login(loginServiceDto.toEntity());
        return LoginResponse.of(jwtTokenProvider.generateAccessTokenAndRefreshToken(
            loginServiceDto.email(), List.of("ROLE_" + UserRole.USER.getRoleName())
        ));
    }
}
