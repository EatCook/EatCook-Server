package com.itcook.cooking.api.global.security.jwt.filter;

import static com.itcook.cooking.api.global.consts.ItCookConstants.ACCESS_TOKEN_HEADER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.BEARER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.REFRESH_TOKEN_HEADER;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.api.global.exception.ApiException;
import com.itcook.cooking.api.global.security.jwt.dto.TokenDto;
import com.itcook.cooking.api.global.security.jwt.service.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
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
        String accessTokenValue = null;
        String refreshTokenValue = null;
        String accessTokenHeader = request.getHeader(ACCESS_TOKEN_HEADER);
        String refreshTokenHeader = request.getHeader(REFRESH_TOKEN_HEADER);

        // TODO access token 만료후 access Token & refresh token 재발급
        if (StringUtils.hasText(refreshTokenHeader) && refreshTokenHeader.startsWith(BEARER)) {
            try {
                refreshTokenValue = refreshTokenHeader.replace(BEARER, "");
                accessTokenValue = accessTokenHeader.replace(BEARER, "");
                log.info("access token 만료로 토큰 재발급");
                TokenDto tokens = jwtTokenProvider.reissue(accessTokenValue, refreshTokenValue);

                sendReissueResponse(response, tokens);
                return;
            } catch (ApiException e) {
                request.setAttribute("exception",e);
                filterChain.doFilter(request,response);
                return;
            }
        }

        // 엑세스 유효성 체크
        if (!StringUtils.hasText(accessTokenHeader) || !accessTokenHeader.startsWith(BEARER)) {
            filterChain.doFilter(request,response);
            return;
        }

        // access token 검증
        verifyAccessToken(accessTokenHeader,request);

        filterChain.doFilter(request,response);
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
            jwtTokenProvider.successAuthentication(accessTokenClaims);
        } catch (ApiException e) {
            request.setAttribute("exception",e);
        }
    }
}
