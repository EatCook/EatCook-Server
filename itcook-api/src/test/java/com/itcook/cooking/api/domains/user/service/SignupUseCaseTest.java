package com.itcook.cooking.api.domains.user.service;

import static com.itcook.cooking.domain.domains.post.domain.enums.CookingType.KOREAN_FOOD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.itcook.cooking.api.IntegrationTestSupport;
import com.itcook.cooking.api.domains.user.dto.request.SignupRequest;
import com.itcook.cooking.api.domains.user.dto.response.UserResponse;
import com.itcook.cooking.api.domains.user.service.dto.AddSignupServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.SendEmailServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.VerifyEmailServiceDto;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.domain.enums.UserRole;
import com.itcook.cooking.domain.domains.user.domain.repository.UserCookingThemeRepository;
import com.itcook.cooking.domain.domains.user.domain.repository.UserRepository;
import com.itcook.cooking.domain.common.events.email.EmailSendEvent;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@RecordApplicationEvents
@Transactional
public class SignupUseCaseTest extends IntegrationTestSupport {

    @Autowired
    private SignupUseCase signupUseCase;

    @Autowired
    private SignupQueryUseCase signupQueryUseCase;

    @Autowired
    private FindUserCase findUserCase;

    @Autowired
    private FindUserQueryUseCase findUserQueryUseCase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCookingThemeRepository userCookingThemeRepository;

    @Autowired
    private ApplicationEvents applicationEvents;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("이메일 인증 코드 검증을 시도한다.")
    void verifyAuthCode() {
        //given
        String authCode = "123456";
        ItCookUser user = createUser("user@test.com", "잇쿡1");
        VerifyEmailServiceDto serviceDto = VerifyEmailServiceDto.builder()
            .email(user.getEmail())
            .authCode(authCode)
            .build();

        given(redisService.getData(anyString())).willReturn(authCode);

        //when
        signupUseCase.verifyAuthCode(serviceDto);

        //then
    }

    @Test
    @DisplayName("이메일 인증 코드가 맞지 않아 예외 발생한다.")
    void verifyAuthCodeNotEqual() {
        //given
        String authCode = "123456";
        ItCookUser user = createUser("user@test.com", "잇쿡1");
        VerifyEmailServiceDto serviceDto = VerifyEmailServiceDto.builder()
            .email(user.getEmail())
            .authCode(authCode)
            .build();

        given(redisService.getData(anyString())).willReturn("123457");

        //when
        //then
        assertThatThrownBy(() -> signupUseCase.verifyAuthCode(serviceDto))
            .isInstanceOf(ApiException.class)
            .hasMessage(UserErrorCode.EMAIL_VERIFY_FAIL.getDescription())
            ;
    }
    @Test
    @DisplayName("이미 회원가입한 이메일이 있어서, 인증 코드 요청시 예외가 발생한다.")
    void verifyAuthCodeDuplicateMail() {
        //given
        String authCode = "123456";
        ItCookUser user = createUser("user@test.com", "잇쿡1");
        SendEmailServiceDto serviceDto = SendEmailServiceDto.builder()
            .email("user@test.com")
            .build();
        given(redisService.getData(anyString())).willReturn(authCode);

        //when
        //then
        assertThatThrownBy(() -> signupQueryUseCase.sendAuthCodeSignup(serviceDto))
            .isInstanceOf(ApiException.class)
            .hasMessage("이미 가입한 유저입니다.")
            ;
    }

    @Test
    @DisplayName("유저 이메일, 비밀번호를 받아서 회원가입을 한다")
    void signup() {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
            .email("user@test.com")
            .password("cook1234")
            .build();

        //when
        UserResponse response = signupUseCase.signup(signupRequest.toServiceDto());

        //then
        ItCookUser savedUser = userRepository.findByEmail(signupRequest.getEmail()).get();
        assertThat(savedUser.getId()).isEqualTo(response.getId());
        assertThat(savedUser.getEmail()).isEqualTo(signupRequest.getEmail());
        assertThat(passwordEncoder.matches(signupRequest.getPassword(), savedUser.getPassword())).isTrue();

    }

    @Test
    @DisplayName("회원가입시, 비밀번호를 입력받지 않아 예외 발생한다.")
    void signupBlankPassword() {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
            .email("usertest.com")
            .password("cook1234")
            .build();

        //when
        assertThatThrownBy(() -> signupUseCase.signup(signupRequest.toServiceDto()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유효한 이메일 형식이 아닙니다.")
            ;


    }

    @Test
    @DisplayName("빈 LifeType과 CookingType을 받아서,추가 회원가입 요청을 시도한다.")
    void addSignupEmpty() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");
        AddSignupServiceDto request = AddSignupServiceDto.builder()
            .email(user.getEmail())
            .cookingType(List.of())
            .nickName("잇쿡2")
            .build();

        //when
        var response = signupUseCase.addSignup(request);

        //then
        ItCookUser findUser = userRepository.findByEmail(user.getEmail()).get();

        assertThat(findUser.getLifeType()).isNull();
        assertThat(findUser.getUserCookingThemes()).isEmpty();

    }
    @Test
    @DisplayName("LifeType,CookingType을 받아서,추가 회원가입 요청을 시도한다.")
    void addSignup() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");
        AddSignupServiceDto request = AddSignupServiceDto.builder()
            .email(user.getEmail())
            .lifeType(LifeType.DIET.getLifeTypeName())
            .cookingType(List.of(KOREAN_FOOD.getCookingTypeName()))
            .nickName("잇쿡2")
            .build();

        //when
        var response = signupUseCase.addSignup(request);

        //then
        ItCookUser findUser = userRepository.findByEmail(user.getEmail()).get();

        assertThat(response.presignedUrl()).isNull();
        assertThat(findUser.getLifeType()).isEqualTo(LifeType.DIET);
        assertThat(findUser.getUserCookingThemes()).hasSize(1)
            .extracting("user", "cookingType")
            .containsExactlyInAnyOrder(
                tuple(findUser, KOREAN_FOOD)
            )
        ;

    }

    @Test
    @DisplayName("지원하지 않는 이미지 확장자로 추가 회원가입시 예외 발생")
    void addSignupNotSupportImageExtension() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");
        String fileExtension = "heic";
        AddSignupServiceDto request = AddSignupServiceDto.builder()
            .email(user.getEmail())
            .fileExtension(fileExtension)
            .lifeType(LifeType.DIET.getLifeTypeName())
            .cookingType(List.of(KOREAN_FOOD.getCookingTypeName()))
            .nickName("잇쿡2")
            .build();

        //when

        //then
        assertThatThrownBy(() -> signupUseCase.addSignup(request))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage(fileExtension + "는 지원하지 않는 확장자입니다.")
            ;
    }

    @Test
    @DisplayName("이메일을 받아서, 계정을 찾는다")
    void findUser() {
        //given
        String savedEmail = "user@test.com";
        createUser(savedEmail, "잇쿡1");
        SendEmailServiceDto serviceDto = SendEmailServiceDto.builder()
            .email(savedEmail)
            .build();

        //when
        findUserQueryUseCase.findUser(serviceDto);

        //then
        // 이벤트 발생 횟수 검증
        long count = applicationEvents.stream(EmailSendEvent.class).count();
        assertThat(count).isEqualTo(1);

        verify(redisService, times(1))
            .setDataWithExpire(anyString(), any(), anyLong());
    }
    @Test
    @DisplayName("이메일을 받아서, 계정을 찾는 시도중에 계정이 없어 예외를 반환")
    void findUserNotFound() {
        //given
        String savedEmail = "user@test.com";
        SendEmailServiceDto serviceDto = SendEmailServiceDto.builder()
            .email(savedEmail)
            .build();

        //when

        //then
        assertThatThrownBy(() -> findUserQueryUseCase.findUser(serviceDto))
            .isInstanceOf(ApiException.class)
            .hasMessage("유저를 찾을 수 없습니다.")
            ;
    }

    @Test
    @DisplayName("계정 정보 찾기 인증 코드 검증시에, 인증 코드 검증에 성공한다.")
    void verifyFindUser() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");
        String existingPassword = user.getPassword();

        VerifyEmailServiceDto serviceDto = VerifyEmailServiceDto.builder()
            .email(user.getEmail())
            .authCode("123456")
            .build();

        given(redisService.getData(anyString())).willReturn("123456");

        //when
        findUserCase.verifyFindUser(serviceDto);

        //then

    }

    @Test
    @DisplayName("계정 정보 찾기 인증 코드 검증시에, 인증 코드 불일치 예외가 발생한다.")
    void verifyFindUserVerifyFail() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");

        VerifyEmailServiceDto serviceDto = VerifyEmailServiceDto.builder()
            .email(user.getEmail())
            .authCode("123456")
            .build();

        given(redisService.getData(anyString())).willReturn("12356");

        //when

        //then
        assertThatThrownBy(() -> findUserCase.verifyFindUser(serviceDto))
            .isInstanceOf(ApiException.class)
            .hasMessage("인증 코드가 일치하지 않습니다.")
        ;
    }


    @Test
    @DisplayName("계정 정보 찾기 인증 코드 검증시에 유효시간 지났거나, null 값이 넘어올 경우 예외를 발생한다.")
    void verifyFindUserNoVerifyCode() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");

        VerifyEmailServiceDto serviceDto = VerifyEmailServiceDto.builder()
            .email(user.getEmail())
            .authCode("123456")
            .build();

        given(redisService.getData(anyString())).willReturn(null);

        //when

        //then
        assertThatThrownBy(() -> findUserCase.verifyFindUser(serviceDto))
            .isInstanceOf(ApiException.class)
            .hasMessage("인증 요청을 먼저 해주세요.")
        ;
    }



    private ItCookUser createUser(String username, String nickName) {
        ItCookUser user = ItCookUser.builder()
            .email(username)
            .password("cook1234")
            .providerType(ProviderType.COMMON)
            .nickName(nickName)
            .userRole(UserRole.USER)
            .build();

        return userRepository.save(user);
    }

}
