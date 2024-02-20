package com.itcook.cooking.api.global.security.jwt;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.itcook.cooking.api.global.errorcode.ErrorCode;
import com.itcook.cooking.api.global.errorcode.UserErrorCode;
import com.itcook.cooking.api.global.exception.ApiException;
import com.itcook.cooking.api.global.security.jwt.service.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Test
    @DisplayName("JWT 토큰 생성 테스트")
    void test1() {
        //given
        //when
        String accessToken = jwtTokenProvider.generateAccessToken("hangjin", of("ROLE_USER"));

        //then
        assertNotNull(accessToken);
    }

    @Test
    @DisplayName("토큰 유효 성공 테스트")
    void test2() {
        //given
        String accessToken = jwtTokenProvider.generateAccessToken("hangjin", of("ROLE_USER"));
        //when
        jwtTokenProvider.isTokenValid(accessToken);
        //then
    }

    @Test
    @DisplayName("토큰 시간 만료시 ApiException(UserErrorCode) 발생 테스트")
    void test3() throws InterruptedException {
        //given
        String accessToken = jwtTokenProvider.generateAccessToken("hangjin", of("ROLE_USER"));
        //when
        TimeUnit.SECONDS.sleep(5);

        //then
        ApiException apiException = assertThrows(ApiException.class,
            () -> jwtTokenProvider.isTokenValid(accessToken));

        ErrorCode errorCode = apiException.getErrorCode();

        assertEquals(errorCode.getHttpStatusCode(), UserErrorCode.TOKEN_EXPIRED.getHttpStatusCode());
        assertEquals(errorCode.getErrorCode(), UserErrorCode.TOKEN_EXPIRED.getErrorCode());

    }

    @Test
    @DisplayName("리프레쉬 토큰 생성")
    void test5() {
        //given
        String refreshToken = jwtTokenProvider.generateRefreshToken("test1");
        String refreshToken1 = jwtTokenProvider.generateRefreshToken("test2");
        String refreshToken2 = jwtTokenProvider.generateRefreshToken("test3");
        String refreshToken3 = jwtTokenProvider.generateRefreshToken("test4");
        //when

        //then
    }

    @Test
    @DisplayName("토큰 유효하지 않을시 ApiException(UserErrorCode) 발생 테스트")
    void test4() {
        //given
        String accessToken = jwtTokenProvider.generateAccessToken("hangjin", of("ROLE_USER"));
        //when

        //then
        ApiException apiException = assertThrows(ApiException.class,
            () -> jwtTokenProvider.isTokenValid(accessToken + "zfaafsd"));
        ErrorCode errorCode = apiException.getErrorCode();

        assertEquals(errorCode.getHttpStatusCode(), UserErrorCode.TOKEN_NOT_VALID.getHttpStatusCode());
        assertEquals(errorCode.getErrorCode(), UserErrorCode.TOKEN_NOT_VALID.getErrorCode());

    }
}