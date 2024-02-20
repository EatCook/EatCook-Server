package com.itcook.cooking.api.global.security.jwt.entrypoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcook.cooking.api.global.dto.ErrorResponse;
import com.itcook.cooking.api.global.errorcode.ErrorCode;
import com.itcook.cooking.api.global.errorcode.UserErrorCode;
import com.itcook.cooking.api.global.exception.ApiException;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
        log.error("jwt 토큰 검증 예외");

        Exception exception = (Exception) request.getAttribute("exception");

        if (exception instanceof ApiException) {
            sendErrorResponse(((ApiException) exception).getErrorCode(),response);
            return;
        }

        sendErrorResponse(UserErrorCode.TOKEN_NOT_EXIST,response);

    }

    private void sendErrorResponse(ErrorCode errorCode, HttpServletResponse response)
        throws IOException {
        ErrorResponse errorResponse = ErrorResponse.ERROR(errorCode);
        String body = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(errorCode.getHttpStatusCode());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(body);
    }
}
