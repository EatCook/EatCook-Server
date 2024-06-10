package com.itcook.cooking.api.global.security.jwt.filter;

import static com.itcook.cooking.api.global.consts.ItCookConstants.ACCESS_TOKEN_HEADER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.BEARER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.REFRESH_TOKEN_HEADER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.ROLES_CLAIM;
import static com.itcook.cooking.api.global.consts.ItCookConstants.USERNAME_CLAIM;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcook.cooking.api.domains.security.AuthenticationUser;
import com.itcook.cooking.api.domains.security.CommonUser;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
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

        // 엑세스 토큰 규격 유효성 체크
        if (!StringUtils.hasText(accessTokenHeader) || !accessTokenHeader.startsWith(BEARER)) {
            filterChain.doFilter(request,response);
            return;
        }

        // access token 검증
        //  엑세스 토큰이 블랙리스트에 있는지 검증 로직 추가 , access token 과 refresh token이 같이 보내는경우
        //  refresh token만 보낼 경우 refresh token이 탈취 당하는 경우 방지
        verifyAccessToken(accessTokenHeader,request);

        filterChain.doFilter(request,response);
    }

    private void reissueTokens(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain, String refreshTokenHeader, String accessTokenHeader)
        throws IOException, ServletException {
        try {
            String refreshTokenValue = refreshTokenHeader.replace(BEARER, "");
            String accessTokenValue = accessTokenHeader.replace(BEARER, "");
            log.info("access token 만료로 토큰 재발급");
            TokenDto tokens = jwtTokenProvider.reissue(accessTokenValue, refreshTokenValue);

            sendReissueResponse(response, tokens);
        } catch (ApiException e) {
            request.setAttribute("exception",e);
            filterChain.doFilter(request, response);
        }
    }

    private void sendReissueResponse(HttpServletResponse response, TokenDto tokens)
        throws IOException {
        ApiResponse apiResponse = ApiResponse.OK("토큰 재발급 성공.");
        String responseBody = objectMapper.writeValueAsString(apiResponse);

        response.setStatus(200);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setHeader(ACCESS_TOKEN_HEADER,BEARER + tokens.getAccessToken());
        response.setHeader(REFRESH_TOKEN_HEADER, BEARER + tokens.getRefreshToken());
        response.getWriter().write(responseBody);
    }

    private void verifyAccessToken(String accessTokenHeader, HttpServletRequest request) {
        try {
            String accessTokenValue = accessTokenHeader.replace(BEARER, "");
            if (jwtTokenProvider.isBlackListToken(accessTokenValue)) {
                throw new ApiException(UserErrorCode.IS_LOGOUT_TOKEN);
            }
            Claims accessTokenClaims = jwtTokenProvider.isTokenValid(accessTokenValue);

            String username = accessTokenClaims.get(USERNAME_CLAIM, String.class);
            if (jwtTokenProvider.isEmailInRedis(username)) {
                throw new ApiException(UserErrorCode.TOKEN_NOT_EXIST);
            }

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

        AuthenticationUser authenticationUser = AuthenticationUser.builder()
            .providerUser(CommonUser.of(username))
            .build();

        UsernamePasswordAuthenticationToken authenticated
            = UsernamePasswordAuthenticationToken.authenticated(
            authenticationUser, null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authenticated);
    }
}
