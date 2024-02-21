package com.itcook.cooking.api.global.security.jwt;

import static com.itcook.cooking.api.global.consts.ItCookConstants.*;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.itcook.cooking.api.global.consts.ItCookConstants;
import com.itcook.cooking.api.global.errorcode.UserErrorCode;
import com.itcook.cooking.api.global.exception.ApiException;
import com.itcook.cooking.api.global.security.jwt.config.RedisTestContainers;
import com.itcook.cooking.api.global.security.jwt.dto.TokenDto;
import com.itcook.cooking.api.global.security.jwt.service.JwtTokenProvider;
import com.itcook.cooking.infra.redis.config.RedisService;
import io.jsonwebtoken.Claims;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest
@Import(RedisTestContainers.class)
public class TokenRedisTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private RedisService redisService;
    private String username = "hangs0908@test.com";

    @Test
    @DisplayName("리프레쉬 토큰 생성 테스트")
    void test2() {
        //given
        String refreshToken = jwtTokenProvider.generateRefreshToken(username);
        //when
        Claims claims = jwtTokenProvider.isTokenValid(refreshToken);

        //then
        assertNotNull(refreshToken);
        assertEquals(username, claims.get(USERNAME_CLAIM));
    }

    @Test
    @DisplayName("리프레쉬 토큰 생성시 redis 저장된 정보 확인 테스트")
    void test3() {
        //given
        TokenDto tokenDto = jwtTokenProvider.generateAccessTokenAndRefreshToken(username,
            of("ROLE_USER"));
        //when
        String value = redisService.getData(username).toString();

        //then
        assertEquals(tokenDto.getRefreshToken(), value);
    }

    @Test
    @DisplayName("토큰 생성후 유효시간 지난 후 조회 테스트")
    void test4() throws InterruptedException {
        //given
        TokenDto tokenDto = jwtTokenProvider.generateAccessTokenAndRefreshToken(username,
            of("ROLE_USER"));
        //when
        TimeUnit.SECONDS.sleep(5L);

        //then
        String value = (String) redisService.getData(username);
        assertNull(value);
    }

    @Test
    @DisplayName("reissue 실패 테스트 (redis 리프래쉬와 전달된 리프래쉬 불일치)")
    void test5() {
        //given
        TokenDto tokenDto1 = jwtTokenProvider.generateAccessTokenAndRefreshToken(username,
            of("ROLE_USER"));
        TokenDto tokenDto2 = jwtTokenProvider.generateAccessTokenAndRefreshToken("test@test.com",
            of("ROLE_USER"));

        //when
        ApiException apiException = assertThrows(ApiException.class,
            () -> jwtTokenProvider.reissue(tokenDto1.getAccessToken(),
                tokenDto2.getRefreshToken()));

        //then
        assertEquals(UserErrorCode.NOT_EQUAL_REFRESH_TOKEN.getErrorCode(), apiException.getErrorCode().getErrorCode());
        assertEquals(UserErrorCode.NOT_EQUAL_REFRESH_TOKEN.getDescription(), apiException.getErrorCode().getDescription());
    }

    @Test
    @DisplayName("reissue 실패 테스트 (블랙리스트 추가 후 redis null point 에러)")
    void test6() throws InterruptedException {
        //given
        TokenDto tokenDto1 = jwtTokenProvider.generateAccessTokenAndRefreshToken(username,
            of("ROLE_USER"));
        TokenDto tokenDto2 = jwtTokenProvider.generateAccessTokenAndRefreshToken("test@test.com",
            of("ROLE_USER"));
        //when
        redisService.deleteData(username);

        //then
        assertThrows(NullPointerException.class , () ->
            jwtTokenProvider.reissue(tokenDto1.getAccessToken(), tokenDto2.getRefreshToken()));

    }

    @Test
    @DisplayName("reissue 테스트")
    void test1() {
        //given
        jwtTokenProvider
            .generateAccessTokenAndRefreshToken("hangs0908@test.com", of("ROLE_USER"));
        //when

        //then
    }
}
