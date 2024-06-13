package com.itcook.cooking.api.global.security.jwt.filter;

import static com.itcook.cooking.api.global.consts.ItCookConstants.ACCESS_TOKEN_HEADER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.BEARER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.REFRESH_TOKEN_HEADER;
import static com.itcook.cooking.api.global.security.jwt.helper.SecurityHelper.sendLoginErrorResponse;
import static com.itcook.cooking.api.global.security.jwt.helper.SecurityHelper.sendTokensSuccessResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.user.dto.request.UserLogin;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.api.global.dto.ErrorResponse;
import com.itcook.cooking.api.global.security.jwt.helper.SecurityHelper;
import com.itcook.cooking.domain.common.errorcode.CommonErrorCode;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.api.global.security.jwt.dto.TokenDto;
import com.itcook.cooking.api.global.security.jwt.service.JwtTokenProvider;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.StreamUtils;

@Slf4j
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtLoginFilter(ObjectMapper objectMapper, JwtTokenProvider jwtTokenProvider) {
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        log.info("attemptAuthentication 로그인 시도");

        try {
            String requestBody = StreamUtils.copyToString(request.getInputStream(),
                StandardCharsets.UTF_8);
            UserLogin userLogin = objectMapper.readValue(requestBody, UserLogin.class);

            UsernamePasswordAuthenticationToken unauthenticated = UsernamePasswordAuthenticationToken
                .unauthenticated(userLogin.getEmail(), userLogin.getPassword());

            return getAuthenticationManager().authenticate(unauthenticated);

        } catch (IOException e) {
            throw new ApiException(CommonErrorCode.SERVER_ERROR);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain, Authentication authResult)
        throws IOException, ServletException {
        log.info("successfulAuthentication 로그인 성공 및 JWT 토큰 발행");

        AuthenticationUser principalUser = (AuthenticationUser) authResult.getPrincipal();
        String username = principalUser.getUsername();
        List<String> authorities = principalUser.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority).toList();

        TokenDto tokenDto = jwtTokenProvider.generateAccessTokenAndRefreshToken(username, authorities);

        sendTokensSuccessResponse(objectMapper, response, "로그인 성공",
            tokenDto.getAccessToken(), tokenDto.getRefreshToken());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, ServletException {
        log.info("unsuccessfulAuthentication 로그인 실패");

        sendLoginErrorResponse(objectMapper, response);
    }

}
