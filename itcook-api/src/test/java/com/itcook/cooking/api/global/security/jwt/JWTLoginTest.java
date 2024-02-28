package com.itcook.cooking.api.global.security.jwt;

import static com.itcook.cooking.api.global.consts.ItCookConstants.ACCESS_TOKEN_HEADER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.REFRESH_TOKEN_HEADER;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcook.cooking.api.domains.user.dto.request.UserLogin;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.domain.common.UserErrorCode;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.api.global.security.jwt.config.RedisTestContainers;
import com.itcook.cooking.api.global.security.jwt.dto.TokenDto;
import com.itcook.cooking.api.global.security.jwt.service.JwtTokenProvider;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import java.net.URI;
import java.net.URISyntaxException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ActiveProfiles("test")
@Import({RedisTestContainers.class})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class JWTLoginTest {

    @LocalServerPort
    private int port;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    private RestTemplate restTemplate = new RestTemplate();

    private URI uri(String path) throws URISyntaxException {
        return new URI(format("http://localhost:%d%s", port, path));
    }

    @BeforeEach
    void init() {
        userRepository.deleteAll();
        userRepository.save(
            ItCookUser.builder()
                .email("hangs0908@test.com")
                .nickName("1234")
                .userRole(UserRole.USER)
                .providerType(ProviderType.COMMON)
                .password(passwordEncoder.encode("1234"))
                .build()
        );
        userRepository.save(
            ItCookUser.builder()
                .email("hangs0908@test1.com")
                .nickName("1234")
                .userRole(UserRole.USER)
                .providerType(ProviderType.COMMON)
                .password(passwordEncoder.encode("1234"))
                .build()
        );
    }

    @Test
    @DisplayName("로그인 성공 테스트")
    void test1() throws URISyntaxException {
        //given
        var login = UserLogin.builder()
            .email("hangs0908@test.com")
            .password("1234")
            .build();

        //when
        HttpEntity<UserLogin> body = new HttpEntity<>(login);
        ResponseEntity<String> response = restTemplate.exchange(uri("/login"), HttpMethod.POST,
            body, String.class);

        //then
        HttpStatus statusCode = response.getStatusCode();
        HttpHeaders headers = response.getHeaders();

        assertEquals(200, statusCode.value());
        assertNotNull(headers.get(ACCESS_TOKEN_HEADER));
        assertNotNull(headers.get(REFRESH_TOKEN_HEADER
        ));
    }

    @Test
    @DisplayName("아이디 또는 비밀번호를 잘못 입력하여 로그인 실패")
    void test2() throws URISyntaxException {
        //given
        var failLogin = UserLogin.builder()
            .email("hangs0908@test.com")
            .password("12345")
            .build();
        //when
        HttpEntity<UserLogin> body = new HttpEntity<>(failLogin);
        HttpClientErrorException httpClientErrorException = assertThrows(
            HttpClientErrorException.class,
            () -> restTemplate.exchange(uri("/login"), HttpMethod.POST,
                body, String.class));

        //then
        assertEquals(HttpStatus.UNAUTHORIZED.value(),httpClientErrorException.getStatusCode().value());

    }

    @Test
    @DisplayName("로그인 성공 후 ACCESS TOKEN 검증 성공 테스트")
    void test3() throws URISyntaxException {
        //given
        String accessToken = getToken("hangs0908@test.com", "1234").getAccessToken();
        //when
        HttpEntity httpEntity = getAuthAccessTokenHeaderEntity(accessToken);
        ResponseEntity<String> response = restTemplate.exchange(uri("/test"), HttpMethod.GET,
            httpEntity, String.class);

        //then
        assertEquals("테스트 성공", response.getBody());
    }

    @Test
    @DisplayName("엑세스 토큰 검증시 유효하지 않은 테스트")
    void test4() throws URISyntaxException, JsonProcessingException {
        //given
        HttpEntity httpEntity = getAuthAccessTokenHeaderEntity("Bearer adsfadsdfassdaf");
        //when
        HttpClientErrorException exception = assertThrows(
            HttpClientErrorException.class,
            () -> restTemplate.exchange(uri("/test"), HttpMethod.GET,
                httpEntity, String.class));

        //then
        String responseBody = exception.getResponseBodyAsString();
        ApiResponse apiResponse = objectMapper.readValue(responseBody, ApiResponse.class);

        assertEquals(401, exception.getStatusCode().value());
        assertEquals(UserErrorCode.TOKEN_NOT_VALID.getErrorCode(),apiResponse.getCode());
        assertEquals(UserErrorCode.TOKEN_NOT_VALID.getDescription(),apiResponse.getMessage());

    }

//    @Test
//    @DisplayName("엑세스 토큰 검증시 토큰 만료시간 초과 테스트")
//    void test5() throws Exception {
//        //given
//        String accessToken = getToken("hangs0908@test.com", "1234").getAccessToken();
//        TimeUnit.SECONDS.sleep(6L);
//        HttpEntity httpEntity = getAuthAccessTokenHeaderEntity(accessToken);
//        //when
//        HttpClientErrorException exception = assertThrows(
//            HttpClientErrorException.class,
//            () -> restTemplate.exchange(uri("/test"), HttpMethod.GET,
//                httpEntity, String.class));
//        String responseBody = exception.getResponseBodyAsString();
//        ApiResponse apiResponse = objectMapper.readValue(responseBody, ApiResponse.class);
//
//        //then
//        assertEquals(UserErrorCode.TOKEN_EXPIRED.getHttpStatusCode(), exception.getStatusCode().value());
//        assertEquals(UserErrorCode.TOKEN_EXPIRED.getErrorCode(), apiResponse.getCode());
//        assertEquals(UserErrorCode.TOKEN_EXPIRED.getDescription(), apiResponse.getMessage());
//    }

    @Test
    @DisplayName("엑세스 토큰이 존재하지 않을시 테스트")
    void test6() throws JsonProcessingException {
        //given
        HttpEntity<String> httpEntity = new HttpEntity<>("", null);

        //when
        HttpClientErrorException exception = assertThrows(
            HttpClientErrorException.class,
            () -> restTemplate.exchange(uri("/test"), HttpMethod.GET,
                httpEntity, String.class));
        String responseBody = exception.getResponseBodyAsString();
        ApiResponse apiResponse = objectMapper.readValue(responseBody, ApiResponse.class);

        //then
        assertEquals(UserErrorCode.TOKEN_NOT_EXIST.getHttpStatusCode(), exception.getStatusCode().value());
        assertEquals(UserErrorCode.TOKEN_NOT_EXIST.getErrorCode(), apiResponse.getCode());
        assertEquals(UserErrorCode.TOKEN_NOT_EXIST.getDescription(), apiResponse.getMessage());
    }

//    @Test
//    @DisplayName("엑세스 토큰 만료 후 토큰 재발급 성공 테스트")
//    void test7() throws URISyntaxException {
//        //given
//        TokenDto tokens = getToken("hangs0908@test.com", "1234");
//        HttpEntity httpEntity = getAuthTokensHeaderEntity(tokens.getAccessToken(), tokens.getRefreshToken());
//        //when
//        ResponseEntity<String> response = restTemplate.exchange(uri("/test"), HttpMethod.GET,
//            httpEntity, String.class);
//
//        //then
//        assertEquals(200,response.getStatusCode().value());
//        assertNotNull(response.getHeaders().get(ACCESS_TOKEN_HEADER));
//        assertNotNull(response.getHeaders().get(REFRESH_TOKEN_HEADER));
//
//    }

    @Test
    @DisplayName("유효하지 않은 리프레쉬 토큰 validation 테스트")
    void test8() throws URISyntaxException, JsonProcessingException {
        //given
        TokenDto token = getToken("hangs0908@test.com", "1234");
        HttpEntity httpEntity = getAuthTokensHeaderEntity(token.getAccessToken(),
            "Bearer asvasbgagdsagd");
        //when
        HttpClientErrorException httpClientErrorException = assertThrows(
            HttpClientErrorException.class,
            () -> restTemplate.exchange(uri("/test"), HttpMethod.GET,
                httpEntity, String.class));

        //then
        String responseBody = httpClientErrorException.getResponseBodyAsString();
        ApiResponse errorResponse = objectMapper.readValue(responseBody, ApiResponse.class);

        assertEquals(UserErrorCode.TOKEN_NOT_VALID.getHttpStatusCode(), httpClientErrorException.getStatusCode().value());
        assertEquals(UserErrorCode.TOKEN_NOT_VALID.getErrorCode(), errorResponse.getCode());
        assertEquals(UserErrorCode.TOKEN_NOT_VALID.getDescription(), errorResponse.getMessage());

    }

    @Test
    @DisplayName("redis에 저장되어 있는 리프레쉬 토큰과 불일치 테스트")
    void test9() throws URISyntaxException, JsonProcessingException, InterruptedException {
        //given
        TokenDto token1 = getToken("hangs0908@test1.com", "1234");
        TokenDto token2 = getToken("hangs0908@test.com", "1234");

        HttpEntity httpEntity = getAuthTokensHeaderEntity(token1.getAccessToken(),
            token2.getRefreshToken());
        //when
        HttpClientErrorException exception = assertThrows(
            HttpClientErrorException.class,
            () -> restTemplate.exchange(uri("/test"), HttpMethod.GET,
                httpEntity, String.class));

        //then
        String responseBody = exception.getResponseBodyAsString();
        ApiResponse apiResponse = objectMapper.readValue(responseBody, ApiResponse.class);
//
        assertEquals(UserErrorCode.NOT_EQUAL_REFRESH_TOKEN.getHttpStatusCode(), exception.getStatusCode().value());
        assertEquals(UserErrorCode.NOT_EQUAL_REFRESH_TOKEN.getErrorCode(), apiResponse.getCode());
        assertEquals(UserErrorCode.NOT_EQUAL_REFRESH_TOKEN.getDescription(), apiResponse.getMessage());
    }

    @Test
    @DisplayName("logout 성공 테스트")
    void testLogout() throws URISyntaxException, JsonProcessingException {
        //given
        String accessToken = getToken("hangs0908@test.com", "1234").getAccessToken();
        HttpEntity httpEntity = getAuthAccessTokenHeaderEntity(accessToken);
        //when
        ResponseEntity<String> response = restTemplate.exchange(uri("/logout"), HttpMethod.GET,
            httpEntity, String.class);

        //then
        String body = response.getBody();
        ApiResponse apiResponse = objectMapper.readValue(body, ApiResponse.class);
        assertEquals("200", apiResponse.getCode());
        assertEquals("로그아웃 성공하였습니다.", apiResponse.getMessage());
    }

    @Test
    @DisplayName("logout 후 black list 추가된 엑세스 토큰으로 조회 실패 테스트")
    void testBlackoutAccessTokenTest() throws URISyntaxException, JsonProcessingException {
        //given
        String accessToken = getToken("hangs0908@test.com", "1234").getAccessToken();
        HttpEntity httpEntity = getAuthAccessTokenHeaderEntity(accessToken);
        restTemplate.exchange(uri("/logout"), HttpMethod.GET,
            httpEntity, String.class);
        //when
        HttpClientErrorException httpClientErrorException = assertThrows(
            HttpClientErrorException.class,
            () -> restTemplate.exchange(uri("/test"), HttpMethod.GET,
                httpEntity, String.class));

        //then
        String responseBody = httpClientErrorException.getResponseBodyAsString();
        ApiResponse apiResponse = objectMapper.readValue(responseBody, ApiResponse.class);

        assertEquals(httpClientErrorException.getStatusCode().value(), UserErrorCode.IS_LOGOUT_TOKEN.getHttpStatusCode());
        assertEquals(UserErrorCode.IS_LOGOUT_TOKEN.getErrorCode(), apiResponse.getCode());
        assertEquals(UserErrorCode.IS_LOGOUT_TOKEN.getDescription(), apiResponse.getMessage());

    }

    @Test
    @DisplayName("로그아웃 후 기존 토큰들로 재발급 시도 테스트")
    void testBlacoutReissue() throws URISyntaxException, JsonProcessingException {
        //given
        TokenDto token = getToken("hangs0908@test1.com", "1234");
        HttpEntity httpEntity = getAuthAccessTokenHeaderEntity(token.getAccessToken());
        ResponseEntity<String> response = restTemplate.exchange(uri("/logout"), HttpMethod.GET,
            httpEntity, String.class);
        //when
        HttpEntity entity = getAuthTokensHeaderEntity(token.getAccessToken(),
            token.getRefreshToken());

        //then
        HttpClientErrorException httpClientErrorException = assertThrows(
            HttpClientErrorException.class,
            () -> restTemplate.exchange(uri("/test"), HttpMethod.GET,
                entity, String.class));
        String responseBody = httpClientErrorException.getResponseBodyAsString();
        ApiResponse apiResponse = objectMapper.readValue(responseBody, ApiResponse.class);

        assertEquals(HttpStatus.UNAUTHORIZED.value(), httpClientErrorException.getStatusCode().value());
        assertEquals(UserErrorCode.TOKEN_NOT_EXIST.getErrorCode(), apiResponse.getCode());
        assertEquals(UserErrorCode.TOKEN_NOT_EXIST.getDescription(), apiResponse.getMessage());
    }

    private TokenDto getToken(String username, String password) throws URISyntaxException {
        UserLogin userLogin = UserLogin.builder()
            .email(username)
            .password(password)
            .build();

        var body = new HttpEntity<>(userLogin);
        var response = restTemplate.exchange(uri("/login"), HttpMethod.POST,
            body, String.class);

        String accessToken = response.getHeaders().get(ACCESS_TOKEN_HEADER).get(0);
        String refreshToken = response.getHeaders().get(REFRESH_TOKEN_HEADER).get(0);

        return TokenDto.builder()
            .accessToken(accessToken)
            .refreshToken(refreshToken)
            .build();
    }

    private HttpEntity getAuthAccessTokenHeaderEntity(String accessToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(ACCESS_TOKEN_HEADER, accessToken);
        return new HttpEntity<>("", httpHeaders);
    }

    private HttpEntity getAuthTokensHeaderEntity(String accessToken, String refreshToken) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(ACCESS_TOKEN_HEADER, accessToken);
        httpHeaders.add(REFRESH_TOKEN_HEADER, refreshToken);
        return new HttpEntity("", httpHeaders);
    }

}
