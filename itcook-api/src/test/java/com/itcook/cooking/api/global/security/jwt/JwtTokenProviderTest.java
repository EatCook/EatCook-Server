package com.itcook.cooking.api.global.security.jwt;

import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.itcook.cooking.api.global.errorcode.CommonErrorCode;
import com.itcook.cooking.api.global.errorcode.ErrorCode;
import com.itcook.cooking.api.global.errorcode.UserErrorCode;
import com.itcook.cooking.api.global.exception.ApiException;
import com.itcook.cooking.api.global.security.jwt.dto.TokenDto;
import com.itcook.cooking.api.global.security.jwt.service.JwtTokenProvider;
import com.itcook.cooking.infra.redis.config.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

class JwtTokenProviderTest {
    private JwtTokenProvider jwtTokenProvider;
    private final RedisService redisService = mock(RedisService.class);
    @BeforeEach
    void init() {
        String key = "testkeytestkeytestkeytestkeytestkeytestkeytestkeytestkeytestkeytestkeytestkeytestkeytestkeytestkey";
        jwtTokenProvider = new JwtTokenProvider(key, redisService);
        ReflectionTestUtils.setField(jwtTokenProvider, "accessExp",3L);
        ReflectionTestUtils.setField(jwtTokenProvider, "refreshExp",5L);
    }

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
    @DisplayName("토큰 존재하지 않을시 validation 테스트")
    void test5() {
        //given
        //when
        ApiException apiException = assertThrows(ApiException.class,
            () -> jwtTokenProvider.isTokenValid(null));

        //then
        assertEquals(CommonErrorCode.BAD_REQUEST.getErrorCode(), apiException.getErrorCode().getErrorCode());
        assertEquals(CommonErrorCode.BAD_REQUEST.getDescription(), apiException.getErrorCode().getDescription());
    }

    @Test
    @DisplayName("tokens(access,refresh) 발행 테스트")
    void test6() {
        //given
        String username = "hangs0908@test.com";

        //when
        TokenDto tokenDto = jwtTokenProvider
            .generateAccessTokenAndRefreshToken(username, of("ROLE_USER"));

        //then
        assertNotNull(tokenDto.getAccessToken());
        assertNotNull(tokenDto.getRefreshToken());
    }

    @Test
    @DisplayName("reissue 성공 테스트")
    void test7() throws InterruptedException {
        //given
        String username = "hangs0908@test.com";
        TokenDto tokenDto = jwtTokenProvider
            .generateAccessTokenAndRefreshToken(username, of("ROLE_USER"));

        when(redisService.getData(username)).thenReturn(tokenDto.getRefreshToken());

        //when
        TokenDto reissueTokens = jwtTokenProvider.reissue(tokenDto.getAccessToken(),
            tokenDto.getRefreshToken());

        //then
        assertNotNull(reissueTokens.getRefreshToken());
        assertNotNull(reissueTokens.getAccessToken());
    }

    @Test
    @DisplayName("reissue 실패(redis에서 null값 반환) 테스트")
    void test8() {
        //given
        String username = "hangs0908@test.com";
        TokenDto tokenDto = jwtTokenProvider
            .generateAccessTokenAndRefreshToken(username, of("ROLE_USER"));

        //when
        when(redisService.getData(username)).thenReturn(null);

        //then
        assertThrows(NullPointerException.class,() -> jwtTokenProvider.reissue(tokenDto.getAccessToken(),
            tokenDto.getRefreshToken()));
    }

    @Test
    @DisplayName("reissue시 access token이 유효하지 않을 경우")
    void test9() {
        //given
        String username = "hangs0908@test.com";
        TokenDto tokenDto = jwtTokenProvider
            .generateAccessTokenAndRefreshToken(username, of("ROLE_USER"));

        //when
        ApiException apiException = assertThrows(ApiException.class,
            () -> jwtTokenProvider.reissue("Bearer afsffdaffsa",
                tokenDto.getRefreshToken() + "aaaa"));

        //then
        assertEquals(UserErrorCode.TOKEN_NOT_VALID.getErrorCode(),apiException.getErrorCode().getErrorCode());
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