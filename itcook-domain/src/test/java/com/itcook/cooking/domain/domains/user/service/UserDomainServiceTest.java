package com.itcook.cooking.domain.domains.user.service;

import static com.itcook.cooking.domain.domains.post.enums.CookingType.CHINESE_FOOD;
import static com.itcook.cooking.domain.domains.post.enums.CookingType.JAPANESE_FOOD;
import static com.itcook.cooking.domain.domains.post.enums.CookingType.KOREAN_FOOD;
import static java.util.List.of;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.itcook.cooking.domain.common.errorcode.ErrorCode;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import com.itcook.cooking.domain.domains.user.enums.LifeType;
import com.itcook.cooking.domain.domains.user.repository.UserCookingThemeJdbcRepository;
import com.itcook.cooking.domain.domains.user.repository.UserCookingThemeRepository;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
class UserDomainServiceTest {

    @InjectMocks
    private UserDomainService userDomainService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCookingThemeJdbcRepository userCookingThemeJdbcRepository;


    @Test
    @DisplayName("이메일 조회 테스트")
    void fetchFindByEmailTest() {
        //given
        given(userRepository.findByEmail("test")).willReturn(Optional.of(
            ItCookUser.builder()
                .id(1L)
                .email("user@test.com")
                .password("cook1234")
                .nickName("test")
                .lifeType(LifeType.DELIVERY_FOOD)
                .build()
        ));
        //when
        ItCookUser itCookUser = userDomainService.fetchFindByEmail("test");

        //then
        assertEquals(1L, itCookUser.getId());
        assertEquals("test", itCookUser.getNickName());
    }

    @Test
    @DisplayName("이메일 조회 실패 테스트")
    void fetchFindByExistingEmailTest() {
        //given
        given(userRepository.findByEmail("test")).willReturn(Optional.empty());

        //when
        ApiException apiException = assertThrows(ApiException.class,
            () -> userDomainService.fetchFindByEmail("test"));

        //then
        assertEquals(UserErrorCode.USER_NOT_FOUND, apiException.getErrorCode());
    }

    @Test
    @DisplayName("추가 회원가입 닉네임 중복 체크 에러 테스트")
    void DuplicateNickNameErrorTest() {
        //given
        ItCookUser user = ItCookUser.builder()
            .id(1L)
            .email("user@test.com")
            .password("cook1234")
            .nickName("test")
            .lifeType(LifeType.DELIVERY_FOOD)
            .build()
            ;

        given(userRepository.findByNickName("test")).willReturn(Optional.of(
            ItCookUser.builder()
                .id(1L)
                .email("user@test.com")
                .password("cook1234")
                .nickName("test")
                .lifeType(LifeType.DELIVERY_FOOD)
                .build()
        ));

        //when
        ApiException apiException = assertThrows(ApiException.class,
            () -> userDomainService.addSignup(user,
                of(KOREAN_FOOD, CHINESE_FOOD,
                    JAPANESE_FOOD)));
        //then
        ErrorCode errorCode = apiException.getErrorCode();
        assertEquals(UserErrorCode.ALREADY_EXISTS_NICKNAME, errorCode);
    }

    @Test
    @DisplayName("추가 회원가입 요청시 아무것도 조회 안됨 에러 테스트")
    void updateNickNameAndLifeTypeErrorTest() {
        //given
        ItCookUser user = ItCookUser.builder()
            .id(1L)
            .email("user@test.com")
            .password("cook1234")
            .nickName("test")
            .lifeType(LifeType.DELIVERY_FOOD)
            .build()
            ;
        List<CookingType> cookingTypes = of(KOREAN_FOOD, CHINESE_FOOD,
            JAPANESE_FOOD);
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        //when
        ApiException apiException = assertThrows(ApiException.class, () ->
            userDomainService.addSignup(user, cookingTypes)
        );

        //then
        assertEquals(UserErrorCode.USER_NOT_FOUND,apiException.getErrorCode());
    }

    @Test
    @DisplayName("추가 회원가입 요청 성공 테스트")
    void addSignupTest() {
        //given
        ItCookUser user = ItCookUser.builder()
            .id(1L)
            .email("user@test.com")
            .password("cook1234")
            .nickName("test")
            .lifeType(LifeType.DELIVERY_FOOD)
            .build()
            ;
        List<CookingType> cookingTypes = of();

        given(userRepository.findByNickName("test")).willReturn(Optional.empty());
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        //when
        ItCookUser itCookUser = userDomainService.addSignup(user, cookingTypes);

        //then
    }

    @Test
    @Disabled
    @DisplayName("추가 회원가입 cookingTypes not null 요청 성공 테스트")
    void addSignupTest2() {
        //given
        ItCookUser user = ItCookUser.builder()
            .id(1L)
            .email("user@test.com")
            .password("cook1234")
            .nickName("test")
            .lifeType(LifeType.DELIVERY_FOOD)
            .build()
            ;
        List<CookingType> cookingTypes = of(KOREAN_FOOD, CHINESE_FOOD,
            JAPANESE_FOOD);
        List<UserCookingTheme> cookingThemes = cookingTypes.stream()
            .map(cookingType -> UserCookingTheme.createUserCookingTheme(1L, cookingType))
            .toList();


        given(userRepository.findByNickName("test")).willReturn(Optional.empty());
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        //when
        ItCookUser itCookUser = userDomainService.addSignup(user, cookingTypes);

        //then
        assertEquals(1L, itCookUser.getId());
        assertEquals("test", itCookUser.getNickName());
    }

}