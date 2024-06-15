package com.itcook.cooking.domain.domains.user.entity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.user.adaptor.UserAdaptor;
import com.itcook.cooking.domain.domains.user.entity.dto.SignupDto;
import com.itcook.cooking.domain.domains.user.entity.validator.UserValidator;
import com.itcook.cooking.domain.domains.user.enums.LifeType;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.enums.UserState;
import com.itcook.cooking.domain.infra.s3.ImageUrlDto;
import java.util.List;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserTest {

    @Mock
    private UserAdaptor userAdaptor;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserImageRegisterService userImageRegisterService;

    @Test
    @DisplayName("이메일, 패스워드, ProviderType을 받아서 회원가입을 시도한다.")
    void signup() {
        //given
        SignupDto signupDto = SignupDto.builder()
            .email("user@test.com")
            .password("cook1234")
            .providerType(ProviderType.COMMON)
            .build();

        //when
        ItCookUser user = ItCookUser.signup(signupDto,
            new UserValidator(userAdaptor, passwordEncoder));

        //then
        assertThat(user.getEmail()).isEqualTo(signupDto.email());
        assertThat(user.getPassword()).isEqualTo(signupDto.password());
        assertThat(user.getProviderType()).isEqualTo(signupDto.providerType());
        assertThat(user.getUserRole()).isEqualTo(UserRole.USER);
        assertThat(user.getUserState()).isEqualTo(UserState.ACTIVE);
    }

    @Test
    @DisplayName("이메일 중복으로 회원가입 시도시 에러가 발생한다")
    void signupDuplicateMail() {
        //given
        String email = "user@test.com";
        SignupDto signupDto = SignupDto.builder()
            .email(email)
            .password("cook1234")
            .providerType(ProviderType.COMMON)
            .build();
        Mockito.doThrow(new ApiException(UserErrorCode.ALREADY_EXISTS_USER))
            .when(userAdaptor).checkDuplicateEmail(email);

        //when
        assertThatThrownBy(
            () -> ItCookUser.signup(signupDto, new UserValidator(userAdaptor, passwordEncoder)))
            .isInstanceOf(ApiException.class)
            .hasMessage(UserErrorCode.ALREADY_EXISTS_USER.getDescription())
        ;
    }

    @Test
    @DisplayName("추가 회원가입을 시도한다")
    void addSignup() {
        //given
        ItCookUser user = ItCookUser.builder()
            .email("user@test.com")
            .password("cook1234")
            .providerType(ProviderType.COMMON)
            .userRole(UserRole.USER)
            .build();
        List<CookingType> cookingTypes = List.of(CookingType.DESERT, CookingType.ASIAN_FOOD);
        given(userImageRegisterService.getImageUrlDto(null, user))
            .willReturn(ImageUrlDto.builder().build())
            ;

        //when
        user.addSignup("잇쿡", LifeType.HEALTH_DIET, cookingTypes, null,
            new UserValidator(userAdaptor, passwordEncoder), userImageRegisterService);


        //then
        assertThat(user.getLifeType()).isEqualTo(LifeType.HEALTH_DIET);
        assertThat(user.getUserCookingThemes()).hasSize(2)
            .extracting("cookingType","user")
            .containsExactlyInAnyOrder(
                Tuple.tuple(CookingType.DESERT, user),
                Tuple.tuple(CookingType.ASIAN_FOOD, user)
            )
        ;
    }
}