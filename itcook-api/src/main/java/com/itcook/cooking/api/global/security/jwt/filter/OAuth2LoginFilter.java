package com.itcook.cooking.api.global.security.jwt.filter;

import static com.itcook.cooking.api.global.consts.ItCookConstants.ACCESS_TOKEN_HEADER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.BEARER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.REFRESH_TOKEN_HEADER;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.security.OAuth2User;
import com.itcook.cooking.api.domains.user.dto.request.UserOAuth2Login;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.api.global.security.jwt.dto.TokenDto;
import com.itcook.cooking.api.global.security.jwt.service.JwtTokenProvider;
import com.itcook.cooking.api.global.security.jwt.service.ProviderUserService;
import com.itcook.cooking.domain.common.errorcode.CommonErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.dto.SignupDto;
import com.itcook.cooking.domain.domains.user.entity.validator.UserValidator;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserValidator userValidator;
    private final ProviderUserService providerUserService;
    private static final String OAUTH_LOGIN_URI = "/oauth2/login";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        if (!OAUTH_LOGIN_URI.equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        log.info("Oauth2 login attemptAuthentication 로그인 시도");

        String requestBody = null;

        try {
            requestBody = StreamUtils.copyToString(request.getInputStream(),
                StandardCharsets.UTF_8);

            UserOAuth2Login userLogin = objectMapper.readValue(requestBody, UserOAuth2Login.class);

            OAuth2User oAuth2User = OAuth2User.of(userLogin.email(), userLogin.providerType());

            signupOauthUser(oAuth2User);
            AuthenticationUser authenticationUser = providerUserService.convert(oAuth2User);
            UsernamePasswordAuthenticationToken authenticated = UsernamePasswordAuthenticationToken
                .authenticated(authenticationUser, null, oAuth2User.getAuthorities());

            makeLoginSuccessResponse(response, authenticated);
        } catch (IOException e) {
            log.error("로그인 에러 : {}", e.getMessage());
            request.setAttribute("exception",
                new ApiException(CommonErrorCode.SERVER_ERROR));
            throw new ApiException(CommonErrorCode.SERVER_ERROR);
        }

    }

    private void signupOauthUser(OAuth2User oAuth2User) {
        userRepository.findByEmail(oAuth2User.getEmail())
            .orElseGet(() -> userRepository.save(
                ItCookUser.signup(SignupDto.of(oAuth2User.getEmail(), oAuth2User.getPassword(),
                    oAuth2User.getProviderType()), userValidator)
            ));

    }

    private void makeLoginSuccessResponse(HttpServletResponse response, Authentication authResult)
        throws IOException {
        ApiResponse apiResponse = ApiResponse.OK("로그인 성공");
        String body = objectMapper.writeValueAsString(apiResponse);

        AuthenticationUser principalUser = (AuthenticationUser) authResult.getPrincipal();
        String username = principalUser.getUsername();
        List<String> authorities = principalUser.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).toList();

        TokenDto tokenDto = jwtTokenProvider.generateAccessTokenAndRefreshToken(username,
            authorities);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addHeader(ACCESS_TOKEN_HEADER, BEARER + tokenDto.getAccessToken());
        response.addHeader(REFRESH_TOKEN_HEADER, BEARER + tokenDto.getRefreshToken());
        response.getWriter().write(body);
    }
}
