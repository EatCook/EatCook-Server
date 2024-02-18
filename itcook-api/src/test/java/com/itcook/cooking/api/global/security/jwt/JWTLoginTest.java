package com.itcook.cooking.api.global.security.jwt;

import static com.itcook.cooking.api.global.consts.ItCookConstants.ACCESS_TOKEN_HEADER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.REFRESH_TOKEN_HEADER;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.itcook.cooking.api.domains.user.dto.request.UserLogin;
import com.itcook.cooking.api.global.consts.ItCookConstants;
import com.itcook.cooking.api.global.errorcode.UserErrorCode;
import com.itcook.cooking.api.global.security.jwt.config.RedisTestContainers;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.jupiter.api.Assertions;
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
@Import(RedisTestContainers.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class JWTLoginTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private RestTemplate restTemplate = new RestTemplate();

    private URI uri(String path) throws URISyntaxException {
        return new URI(format("http://localhost:%d%s", port, path));
    }

    @BeforeEach
    void init() {
        userRepository.save(
            ItCookUser.builder()
                .email("hangs0908@test.com")
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

}
