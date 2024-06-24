package com.itcook.cooking.api.global.security.jwt.filter;

import static com.itcook.cooking.api.global.consts.ItCookConstants.ACCESS_TOKEN_HEADER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.BEARER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.USERNAME_CLAIM;

import com.itcook.cooking.api.global.security.jwt.service.JwtTokenProvider;
import com.itcook.cooking.domain.domains.infra.redis.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.lang.Assert;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) {
        log.info("로그아웃 시도");

        String accessTokenHeader = request.getHeader(ACCESS_TOKEN_HEADER);
        Assert.notNull(accessTokenHeader, "Access Token이 Null 입니다.");
        Assert.state(accessTokenHeader.startsWith(BEARER),"access token은 Bearer 로 시작해야합니다");

        String accessTokenValue = accessTokenHeader.replace(BEARER, "");
        Claims claims = jwtTokenProvider.parseAccessToken(accessTokenValue);

        String username = claims.get(USERNAME_CLAIM, String.class);
        long expirationTime = claims.getExpiration().getTime();
        long currentTime = new Date().getTime();

        if (expirationTime - currentTime > 0) {
            redisService.addBlackList(accessTokenValue, expirationTime - currentTime);
        }
        redisService.deleteData(username);
        SecurityContextHolder.clearContext();


    }
}
