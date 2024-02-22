package com.itcook.cooking.api.global.security.jwt.filter;

import static com.itcook.cooking.api.global.consts.ItCookConstants.ACCESS_TOKEN_HEADER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.BEARER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.REFRESH_TOKEN_HEADER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.ROLES_CLAIM;
import static com.itcook.cooking.api.global.consts.ItCookConstants.USERNAME_CLAIM;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.api.global.exception.ApiException;
import com.itcook.cooking.api.global.security.jwt.dto.TokenDto;
import com.itcook.cooking.api.global.security.jwt.service.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import java.util.List;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtCheckFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    /**
     * access token 만료응답
     *      -> access token, refresh token 전송
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        log.info("doFilterInternal JWT 토큰 체크");
        String accessTokenHeader = request.getHeader(ACCESS_TOKEN_HEADER);
        String refreshTokenHeader = request.getHeader(REFRESH_TOKEN_HEADER);

        // access token 만료후 access Token & refresh token 재발급
        if (StringUtils.hasText(refreshTokenHeader) && refreshTokenHeader.startsWith(BEARER)) {
            reissueTokens(request, response, filterChain, refreshTokenHeader, accessTokenHeader);
            return;
        }

        // 엑세스 유효성 체크
        if (!StringUtils.hasText(accessTokenHeader) || !accessTokenHeader.startsWith(BEARER)) {
            filterChain.doFilter(request,response);
            return;
        }

        // access token 검증
        // TODO 로그아웃 구현시 블랙리스트 추가
        //  엑세스 토큰이 블랙리스트에 있는지 검증 로직 추가 , access token 과 refresh token이 같이 보내는경우
        //  refresh token만 보낼 경우 refresh token이 탈취 당하는 경우 방지
        verifyAccessToken(accessTokenHeader,request);

        filterChain.doFilter(request,response);
    }

    private void reissueTokens(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain, String refreshTokenHeader, String accessTokenHeader)
        throws IOException, ServletException {
        String accessTokenValue;
        String refreshTokenValue;
        try {
            refreshTokenValue = refreshTokenHeader.replace(BEARER, "");
            accessTokenValue = accessTokenHeader.replace(BEARER, "");
            log.info("access token 만료로 토큰 재발급");
            TokenDto tokens = jwtTokenProvider.reissue(accessTokenValue, refreshTokenValue);

            sendReissueResponse(response, tokens);
            return;
        } catch (ApiException e) {
            request.setAttribute("exception",e);
            filterChain.doFilter(request, response);
            return;
        }
    }

    private void sendReissueResponse(HttpServletResponse response, TokenDto tokens)
        throws IOException {
        ApiResponse apiResponse = ApiResponse.OK("토큰 재발급 성공.");
        String responseBody = objectMapper.writeValueAsString(apiResponse);

        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(ACCESS_TOKEN_HEADER,tokens.getAccessToken());
        response.setHeader(REFRESH_TOKEN_HEADER, tokens.getRefreshToken());
        response.getWriter().write(responseBody);
    }

    private void verifyAccessToken(String accessTokenHeader, HttpServletRequest request) {
        try {
            String accessTokenValue;
            accessTokenValue = accessTokenHeader.replace(BEARER, "");
            Claims accessTokenClaims = jwtTokenProvider.isTokenValid(accessTokenValue);
            successAuthentication(accessTokenClaims);
        } catch (ApiException e) {
            request.setAttribute("exception",e);
        }
    }

    private void successAuthentication(Claims tokenClaims) {
        String username = tokenClaims.get(USERNAME_CLAIM, String.class);
        List<String> roles = tokenClaims.get(ROLES_CLAIM, List.class);

        List<SimpleGrantedAuthority> authorities = roles.stream()
            .map(SimpleGrantedAuthority::new)
            .toList();

        UserDetails user = User.withUsername(username)
            .password("")
            .authorities(authorities)
            .build();

        UsernamePasswordAuthenticationToken authenticated
            = UsernamePasswordAuthenticationToken.authenticated(
            user, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticated);
    }
}
