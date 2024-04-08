package com.itcook.cooking.domain.domains.user.entity;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ItCookUserTest {

    @Test
    @DisplayName("올바른 유저 객체 생성")
    void createUserCorrect() {
        //given
        ItCookUser user = ItCookUser.builder()
            .email("cook1234@test.com")
            .password("cook1234")
            .nickName("잇쿡1")
            .userRole(UserRole.USER)
            .providerType(ProviderType.COMMON)
            .build();

        //when

        //then
    }

    @Test
    @DisplayName("빌더로 User 객체 생성(Email 비어있을시 예외 발생)")
    void createUser() {
        //given


        //when

        //then
        assertThatThrownBy(() -> ItCookUser.builder()
            .password("cook1234")
            .nickName("잇쿡1")
            .userRole(UserRole.USER)
            .providerType(ProviderType.COMMON)
            .build())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Email is Not Empty")
        ;
    }

    @Test
    @DisplayName("빌더로 User 객체 생성. 이메일 형식이 안맞아서 예외 발생")
    void createUser2() {
        //given


        //when

        //then
        assertThatThrownBy(() -> ItCookUser.builder()
            .email("cook1234@test")
            .password("cook1234")
            .nickName("잇쿡1")
            .userRole(UserRole.USER)
            .providerType(ProviderType.COMMON)
            .build())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유효한 이메일 형식이 아닙니다")
        ;
    }

    @Test
    @DisplayName("빌더로 User 객체 생성. 비밀번호 비어있을시 예외 발생")
    void createUser3() {
        //given


        //when

        //then
        assertThatThrownBy(() -> ItCookUser.builder()
            .email("cook1234@test.com")
            .nickName("잇쿡1")
            .userRole(UserRole.USER)
            .providerType(ProviderType.COMMON)
            .build())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("Password is Not Empty")
        ;
    }

    @Test
    @DisplayName("빌더로 User 객체 생성. 비밀번호는 8자리 이상이며 영문,숫자 혼합")
    void createUser4() {
        //given


        //when

        //then
        assertThatThrownBy(() -> ItCookUser.builder()
            .email("cook1234@test.com")
            .password("cook123")
            .nickName("잇쿡1")
            .userRole(UserRole.USER)
            .providerType(ProviderType.COMMON)
            .build())
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("패스워드는 8자리 이상이어야 하며, 영문과 숫자를 포함해야 합니다.")
        ;
    }


}