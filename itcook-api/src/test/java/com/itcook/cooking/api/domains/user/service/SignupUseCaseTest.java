package com.itcook.cooking.api.domains.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.itcook.cooking.api.domains.user.dto.request.AddSignupRequest;
import com.itcook.cooking.api.domains.user.dto.request.SignupRequest;
import com.itcook.cooking.api.domains.user.dto.request.VerifyEmailAuthRequest;
import com.itcook.cooking.api.domains.user.dto.response.AddUserResponse;
import com.itcook.cooking.api.domains.user.dto.response.UserResponse;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.LifeType;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import com.itcook.cooking.infra.redis.RedisService;
import com.itcook.cooking.infra.s3.ImageUrlDto;
import com.itcook.cooking.infra.s3.S3PresignedUrlService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class SignupUseCaseTest {

    @InjectMocks
    private SignupUseCase signupUseCase;

    @Mock
    private UserDomainService userDomainService;

    @Mock
    private RedisService redisService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private S3PresignedUrlService s3PresignedUrlService;

    @Test
    @DisplayName("이메일 인증 코드 검증 성공 테스트")
    void sendEmailCodeTest() {
        //given
        VerifyEmailAuthRequest verifyEmailAuthRequest = VerifyEmailAuthRequest.builder()
            .email("user@naver.com")
            .authCode("1234")
                .build();
        given(redisService.getData(verifyEmailAuthRequest.getEmail())).willReturn("1234");
        //when
        signupUseCase.verifyAuthCode(verifyEmailAuthRequest);

        //then
    }

    @Test
    @DisplayName("이메일 검증시 Redis에 null 반환 예외 테스트")
    void getNullToRedis() {
        //given
        VerifyEmailAuthRequest verifyEmailAuthRequest = VerifyEmailAuthRequest.builder()
            .email("user@naver.com")
            .authCode("1234")
            .build();

        //when
        given(redisService.getData(verifyEmailAuthRequest.getEmail())).willReturn(null);

        //then
        ApiException apiException = assertThrows(ApiException.class,
            () -> signupUseCase.verifyAuthCode(verifyEmailAuthRequest));

        assertEquals(UserErrorCode.NO_VERIFY_CODE, apiException.getErrorCode());
    }

    @Test
    @DisplayName("이메일 검증시 인증 코드 불일치")
    void notEqualAuthCode() {
        //given
        VerifyEmailAuthRequest verifyEmailAuthRequest = VerifyEmailAuthRequest.builder()
            .email("user@naver.com")
            .authCode("1234")
            .build();

        //when
        given(redisService.getData(verifyEmailAuthRequest.getEmail())).willReturn("4321");

        //then
        ApiException apiException = assertThrows(ApiException.class,
            () -> signupUseCase.verifyAuthCode(verifyEmailAuthRequest));

        assertEquals(UserErrorCode.EMAIL_VERIFY_FAIL, apiException.getErrorCode());
    }

    @Test
    @DisplayName("회원가입 성공 테스트")
    void successSignup() {
        //given
        SignupRequest signupRequest = SignupRequest.builder()
            .email("user@test.com")
            .password("1234")
            .build();
        given(passwordEncoder.encode(signupRequest.getPassword())).willReturn("1234");

        given(userDomainService.registerUser(any(ItCookUser.class))).willReturn(ItCookUser.builder()
                .id(1L)
                .email(signupRequest.getEmail())
                .password(signupRequest.getPassword())
                .providerType(ProviderType.COMMON)
                .userRole(UserRole.USER)
                .build());

        //when
        UserResponse userResponse = signupUseCase
            .signup(signupRequest);

        //then
        assertEquals(1L, userResponse.getId());
        assertEquals(signupRequest.getEmail(), userResponse.getEmail());
    }

    @Test
    @DisplayName("추가 회원가입 요청(확장자 없는) 테스트")
    void addSignupTest() {
        //given
        AddSignupRequest addSignupRequest = AddSignupRequest.builder()
            .userId(1L)
            .nickName("잇쿡")
            .cookingType(List.of("한식", "중식", "일식"))
            .lifeType("배달음식 단골고객")
            .build();
        given(userDomainService.addSignup(any(ItCookUser.class), any(List.class)))
            .willReturn(ItCookUser.builder()
                .id(1L)
                .nickName(addSignupRequest.getNickName())
                .lifeType(LifeType.DELIVERY_FOOD)
                .build());

        //when
        AddUserResponse addUserResponse = signupUseCase.addSignup(addSignupRequest);
        //then

        assertEquals(addUserResponse.getUserId(), 1L);
        assertNull(addUserResponse.getPresignedUrl());
    }

    @Test
    @DisplayName("추가 회원가입 요청(확장자 있는) 테스트")
    void addSignupTestWithImageExtension() {
        //given
        AddSignupRequest addSignupRequest = AddSignupRequest.builder()
            .userId(1L)
            .nickName("잇쿡")
            .fileExtension("jpg")
            .cookingType(List.of("한식", "중식", "일식"))
            .lifeType("배달음식 단골고객")
            .build();
        given(userDomainService.addSignup(any(ItCookUser.class), any(List.class)))
            .willReturn(ItCookUser.builder()
                .id(1L)
                .nickName(addSignupRequest.getNickName())
                .lifeType(LifeType.DELIVERY_FOOD)
                .build());
        given(s3PresignedUrlService.forUser(1L, "jpg"))
            .willReturn(ImageUrlDto.builder()
                .url("http://presignedUrl.com")
                .key("저장된 파일 경로")
                .build());
        //when
        AddUserResponse addUserResponse = signupUseCase.addSignup(addSignupRequest);

        //then
        assertEquals(addUserResponse.getUserId(), 1L);
        assertEquals(addUserResponse.getPresignedUrl(), "http://presignedUrl.com");

    }

    @Test
    @DisplayName("지원하지 않는 확장자 테스트")
    void addSignupTestWithImageExtensionError() {
        //given
        AddSignupRequest addSignupRequest = AddSignupRequest.builder()
            .userId(1L)
            .nickName("잇쿡")
            .fileExtension("heic")
            .cookingType(List.of("한식", "중식", "일식"))
            .lifeType("배달음식 단골고객")
            .build();
        given(userDomainService.addSignup(any(ItCookUser.class), any(List.class)))
            .willReturn(ItCookUser.builder()
                .id(1L)
                .nickName(addSignupRequest.getNickName())
                .lifeType(LifeType.DELIVERY_FOOD)
                .build());
        //when
        IllegalArgumentException illegalArgumentException = assertThrows(
            IllegalArgumentException.class, () -> signupUseCase.addSignup(addSignupRequest));

        //then
        assertEquals("heic는 지원하지 않는 확장자입니다.", illegalArgumentException.getMessage());

    }
}
