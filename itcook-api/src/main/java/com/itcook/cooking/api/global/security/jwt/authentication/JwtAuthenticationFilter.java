package com.itcook.cooking.api.global.security.jwt.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcook.cooking.api.global.security.jwt.service.JwtTokenProvider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(ObjectMapper objectMapper, JwtTokenProvider jwtTokenProvider) {
        this.objectMapper = objectMapper;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {
        return super.attemptAuthentication(request, response);
    }
}
