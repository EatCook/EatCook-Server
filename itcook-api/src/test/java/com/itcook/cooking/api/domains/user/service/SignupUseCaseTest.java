package com.itcook.cooking.api.domains.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.itcook.cooking.api.IntegrationTestSupport;
import com.itcook.cooking.api.domains.user.dto.request.SignupRequest;
import com.itcook.cooking.api.domains.user.dto.response.UserResponse;
import com.itcook.cooking.api.domains.user.service.dto.SendEmailServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.VerifyEmailServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.response.VerifyFindUserResponse;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import com.itcook.cooking.infra.email.EmailSendEvent;
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
    private UserRepository userRepository;

    @Autowired
    private ApplicationEvents applicationEvents;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Test
    @DisplayName("유저 이메일, 비밀번호를 받아서 회원가입을 한다")
    void signup() {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
            .email("user@test.com")
            .password("cook1234")
            .build();

        //when
        UserResponse response = signupUseCase.signup(signupRequest);

        //then
        ItCookUser savedUser = userRepository.findByEmail(signupRequest.getEmail()).get();
        assertThat(savedUser.getId()).isEqualTo(response.getId());
        assertThat(savedUser.getEmail()).isEqualTo(signupRequest.getEmail());
        assertThat(savedUser.getPassword()).isNotEqualTo(signupRequest.getPassword());

    }

    @Test
    @DisplayName("유저 이메일, 비밀번호를 받아서 회원가입을 한다")
    void signupBlankPassword() {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
            .email("usertest.com")
            .password("cook1234")
            .build();

        //when
        assertThatThrownBy(() -> signupUseCase.signup(signupRequest))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("유효한 이메일 형식이 아닙니다.")
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
        signupUseCase.findUser(serviceDto);

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
        assertThatThrownBy(() -> signupUseCase.findUser(serviceDto))
            .isInstanceOf(ApiException.class)
            .hasMessage("유저를 찾을 수 없습니다.")
            ;
    }

    @Test
    @DisplayName("authCode를 검증하여, 유저의 임시 비밀번호를 생성한다.")
    void verifyFindUser() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");

        VerifyEmailServiceDto serviceDto = VerifyEmailServiceDto.builder()
            .email(user.getEmail())
            .authCode("123456")
            .build();

        given(redisService.getData(anyString())).willReturn("123456");

        //when
        VerifyFindUserResponse response = signupUseCase.verifyFindUser(serviceDto);

        //then
        ItCookUser itCookUser = userRepository.findByEmail(user.getEmail()).get();

        assertThat(passwordEncoder.matches(response.password(), itCookUser.getPassword())).isTrue()
        ;
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
        assertThatThrownBy(() -> signupUseCase.verifyFindUser(serviceDto))
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
        assertThatThrownBy(() -> signupUseCase.verifyFindUser(serviceDto))
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
