package com.itcook.cooking.domain.domains.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.itcook.cooking.domain.common.errorcode.ErrorCode;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.post.repository.CookingThemeRepository;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser.ItCookUserBuilder;
import com.itcook.cooking.domain.domains.user.enums.LifeType;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserCookingThemeRepository;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UserDomainServiceTest {

    @InjectMocks
    private UserDomainService userDomainService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCookingThemeRepository userCookingThemeRepository;

//    @BeforeEach
//    void setUp() {
//    }


    @Test
    @DisplayName("추가 회원가입 닉네임 중복 체크 에러 테스트")
    void addSignupTest() {
        //given
        ItCookUser user = ItCookUser.builder()
            .id(1L)
            .nickName("test")
            .lifeType(LifeType.DELIVERY_FOOD)
            .build()
            ;

        given(userRepository.findByNickName("test")).willReturn(Optional.of(
            ItCookUser.builder()
                .id(1L)
                .nickName("test")
                .lifeType(LifeType.DELIVERY_FOOD)
                .build()
        ));

        //when
        ApiException apiException = assertThrows(ApiException.class,
            () -> userDomainService.addSignup(user,
                List.of(CookingType.KOREAN_FOOD, CookingType.CHINESE_FOOD,
                    CookingType.JAPANESE_FOOD)));
        //then
        ErrorCode errorCode = apiException.getErrorCode();
        assertEquals(UserErrorCode.ALREADY_EXISTS_NICKNAME, errorCode);
    }

    @Test
    @DisplayName("추가 회원가입 요청시 아무것도 조회 안됨 에러 테스트")
    void updateNickNameAndLifeTypeTest() {
        //given
        ItCookUser user = ItCookUser.builder()
            .id(1L)
            .nickName("test")
            .lifeType(LifeType.DELIVERY_FOOD)
            .build()
            ;
        List<CookingType> cookingTypes = List.of(CookingType.KOREAN_FOOD, CookingType.CHINESE_FOOD,
            CookingType.JAPANESE_FOOD);
        given(userRepository.findById(1L)).willReturn(Optional.empty());

        //when
        ApiException apiException = assertThrows(ApiException.class, () ->
            userDomainService.addSignup(user, cookingTypes)
        );

        //then
        assertEquals(UserErrorCode.USER_NOT_FOUND,apiException.getErrorCode());
    }

}