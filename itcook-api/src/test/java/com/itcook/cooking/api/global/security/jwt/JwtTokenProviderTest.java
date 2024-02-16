package com.itcook.cooking.api.global.security.jwt;

import com.itcook.cooking.api.global.security.jwt.service.JwtTokenProvider;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class JwtTokenProviderTest {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Test
    @DisplayName("JWT 토큰 생성 테스트")
    void generateAccessTokenTest() {
        //given
        //when
        String accessToken = jwtTokenProvider.generateAccessToken("hangjin", List.of("ROLE_USER"));

        //then
        System.out.println("accessToken = " + accessToken);
    }
}