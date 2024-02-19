package com.itcook.cooking.api.global.security.jwt.service;

import static com.itcook.cooking.api.global.consts.ItCookConstants.ACCESS_TOKEN_SUBJECT;
import static com.itcook.cooking.api.global.consts.ItCookConstants.REFRESH_TOKEN_SUBJECT;
import static com.itcook.cooking.api.global.consts.ItCookConstants.ROLES_CLAIM;
import static com.itcook.cooking.api.global.consts.ItCookConstants.USERNAME_CLAIM;
import static io.jsonwebtoken.SignatureAlgorithm.*;

import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.global.errorcode.CommonErrorCode;
import com.itcook.cooking.api.global.errorcode.UserErrorCode;
import com.itcook.cooking.api.global.exception.ApiException;
import com.itcook.cooking.api.global.security.jwt.dto.TokenDto;
import com.itcook.cooking.infra.redis.config.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Slf4j
@Service

public class JwtTokenProvider {

    @Value("${jwt.access-exp}")
    private Long accessExp;
    @Value("${jwt.refresh-exp}")
    private Long refreshExp;
    private final Key key;
    private final RedisService redisService;

    public JwtTokenProvider(
        @Value("${jwt.secret-key}") String secretKey,
        RedisService redisService
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.redisService = redisService;
    }


    public TokenDto generateAccessTokenAndRefreshToken(Authentication authResult) {

        AuthenticationUser principalUser = (AuthenticationUser) authResult.getPrincipal();
        String username = principalUser.getUsername();
        List<String> authorities = principalUser.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).toList();

        String accessToken = generateAccessToken(username, authorities);
        String refreshToken = generateRefreshToken();

        redisService.setDataWithExpire(username, refreshToken, refreshExp);

        return TokenDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }


    public String generateAccessToken(String username, List<String> authorities) {
        return Jwts.builder()
            .setSubject(ACCESS_TOKEN_SUBJECT)
            .claim(USERNAME_CLAIM, username)
            .claim(ROLES_CLAIM, authorities)
            .setExpiration(new Date(new Date().getTime() + accessExp * 1000L))
            .signWith(key, HS512)
            .compact()
            ;
    }

    public String generateRefreshToken() {
        return Jwts.builder()
            .setSubject(REFRESH_TOKEN_SUBJECT)
            .setExpiration(new Date(new Date().getTime() + refreshExp * 1000L))
            .signWith(key, HS512)
            .compact()
            ;
    }

    public Claims isTokenValid(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                ;
        } catch (ExpiredJwtException e) {
            throw new ApiException(UserErrorCode.TOKEN_EXPIRED);
        } catch (JwtException e) {
            throw new ApiException(UserErrorCode.TOKEN_NOT_VALID);
        } catch (Exception e) {
            throw new ApiException(CommonErrorCode.BAD_REQUEST);
        }
    }

}
