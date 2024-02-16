package com.itcook.cooking.api.global.security.jwt.service;

import static com.itcook.cooking.api.global.consts.ItCookConstants.ACCESS_TOKEN_SUBJECT;
import static com.itcook.cooking.api.global.consts.ItCookConstants.ROLES_CLAIM;
import static com.itcook.cooking.api.global.consts.ItCookConstants.USERNAME_CLAIM;

import com.itcook.cooking.api.global.consts.ItCookConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class JwtTokenProvider {

    @Value("${jwt.access-exp}")
    private Long accessExp;
    @Value("${jwt.refresh-exp}")
    private Long refreshExp;
    private final Key key;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }


    public String generateAccessToken(String username, List<String> authorities) {
        return Jwts.builder()
            .setSubject(ACCESS_TOKEN_SUBJECT)
            .claim(USERNAME_CLAIM, username)
            .claim(ROLES_CLAIM, authorities)
            .setExpiration(new Date(new Date().getTime() + accessExp * 1000L))
            .signWith(key)
            .compact()
            ;
    }

    public Claims isTokenValid(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJwt(token)
            .getBody();
    }

}
