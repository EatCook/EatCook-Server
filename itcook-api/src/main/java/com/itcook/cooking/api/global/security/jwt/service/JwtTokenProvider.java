package com.itcook.cooking.api.global.security.jwt.service;

import static com.itcook.cooking.api.global.consts.ItCookConstants.ACCESS_TOKEN_SUBJECT;
import static com.itcook.cooking.api.global.consts.ItCookConstants.REFRESH_TOKEN_SUBJECT;
import static com.itcook.cooking.api.global.consts.ItCookConstants.ROLES_CLAIM;
import static com.itcook.cooking.api.global.consts.ItCookConstants.TOKEN_ISSUER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.USERNAME_CLAIM;
import static io.jsonwebtoken.SignatureAlgorithm.HS512;

import com.itcook.cooking.api.global.security.jwt.dto.TokenDto;
import com.itcook.cooking.api.global.security.jwt.properties.JwtProperties;
import com.itcook.cooking.domain.common.errorcode.CommonErrorCode;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.infra.redis.RedisService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final RedisService redisService;
    private final JwtProperties jwtProperties;


    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }


    public TokenDto generateAccessTokenAndRefreshToken(String username, List<String> authorities) {
        String accessToken = generateAccessToken(username, authorities);
        String refreshToken = generateRefreshToken(username);

        redisService.setDataWithExpire(username, refreshToken, jwtProperties.getRefreshExp());

        return TokenDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }


    public String generateAccessToken(String username, List<String> authorities) {
        Key encodedKey = getSecretKey();
        return Jwts.builder()
            .setIssuer(TOKEN_ISSUER)
            .setSubject(ACCESS_TOKEN_SUBJECT)
            .setIssuedAt(new Date())
            .claim(USERNAME_CLAIM, username)
            .claim(ROLES_CLAIM, authorities)
            .setExpiration(new Date(new Date().getTime() + jwtProperties.getAccessExp() * 1000L))
            .signWith(encodedKey, HS512)
            .compact()
            ;
    }

    public String generateRefreshToken(String username) {
        Key encodedKey = getSecretKey();
        return Jwts.builder()
            .setIssuer(TOKEN_ISSUER)
            .setSubject(REFRESH_TOKEN_SUBJECT)
            .setIssuedAt(new Date())
            .claim(USERNAME_CLAIM, username)
            .setExpiration(new Date(new Date().getTime() + jwtProperties.getRefreshExp() * 1000L))
            .signWith(encodedKey, HS512)
            .compact()
            ;
    }

    public Claims isTokenValid(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
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
                .setSigningKey(getSecretKey())
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

    public boolean isEmailInRedis(String email) {
        return redisService.getData(email) == null;
    }
}
