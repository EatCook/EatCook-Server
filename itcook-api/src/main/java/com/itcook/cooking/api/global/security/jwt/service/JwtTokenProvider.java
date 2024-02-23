package com.itcook.cooking.api.global.security.jwt.service;

import static com.itcook.cooking.api.global.consts.ItCookConstants.ACCESS_TOKEN_SUBJECT;
import static com.itcook.cooking.api.global.consts.ItCookConstants.REFRESH_TOKEN_SUBJECT;
import static com.itcook.cooking.api.global.consts.ItCookConstants.ROLES_CLAIM;
import static com.itcook.cooking.api.global.consts.ItCookConstants.TOKEN_ISSUER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.USERNAME_CLAIM;
import static io.jsonwebtoken.SignatureAlgorithm.*;

import com.itcook.cooking.api.global.errorcode.CommonErrorCode;
import com.itcook.cooking.api.global.errorcode.UserErrorCode;
import com.itcook.cooking.api.global.exception.ApiException;
import com.itcook.cooking.api.global.security.jwt.dto.TokenDto;
import com.itcook.cooking.infra.redis.config.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtTokenProvider {

    private final Long accessExp;
    private final Long refreshExp;
    private final Key key;
    private final RedisService redisService;

    public JwtTokenProvider(
        @Value("${jwt.secret-key}") String secretKey,
        @Value("${jwt.access-exp}") Long accessExp,
        @Value("${jwt.refresh-exp}") Long refreshExp,
        RedisService redisService
    ) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.redisService = redisService;
        this.accessExp = accessExp;
        this.refreshExp = refreshExp;
    }


    public TokenDto generateAccessTokenAndRefreshToken(String username, List<String> authorities) {
        String accessToken = generateAccessToken(username, authorities);
        String refreshToken = generateRefreshToken(username);

        redisService.setDataWithExpire(username, refreshToken, refreshExp);

        return TokenDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }


    public String generateAccessToken(String username, List<String> authorities) {
        return Jwts.builder()
            .setIssuer(TOKEN_ISSUER)
            .setSubject(ACCESS_TOKEN_SUBJECT)
            .setIssuedAt(new Date())
            .claim(USERNAME_CLAIM, username)
            .claim(ROLES_CLAIM, authorities)
            .setExpiration(new Date(new Date().getTime() + accessExp * 1000L))
            .signWith(key, HS512)
            .compact()
            ;
    }

    public String generateRefreshToken(String username) {
        return Jwts.builder()
            .setIssuer(TOKEN_ISSUER)
            .setSubject(REFRESH_TOKEN_SUBJECT)
            .setIssuedAt(new Date())
            .claim(USERNAME_CLAIM, username)
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

    public TokenDto reissue(String accessTokenValue, String refreshTokenValue) {
        //refresh token 검증
        isTokenValid(refreshTokenValue);
        // 만료된 access token에서 username, authorities 추출
        Claims accessTokenClaims = parseAccessToken(accessTokenValue);
        String username = accessTokenClaims.get(USERNAME_CLAIM, String.class);
        List<String> authorities = accessTokenClaims.get(ROLES_CLAIM, List.class);

        String savedRefreshToken = (String) redisService.getData(username);

        if (!savedRefreshToken.equals(refreshTokenValue)) {
            redisService.deleteData(username);
            throw new ApiException(UserErrorCode.NOT_EQUAL_REFRESH_TOKEN);
        }

        return generateAccessTokenAndRefreshToken(username, authorities);
    }

    public Claims parseAccessToken(String accessToken) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        } catch (JwtException e) {
            throw new ApiException(UserErrorCode.TOKEN_NOT_VALID);
        }
    }


    public boolean isBlackListToken(String accessToken) {
        return redisService.getData(accessToken) != null;
    }
}
