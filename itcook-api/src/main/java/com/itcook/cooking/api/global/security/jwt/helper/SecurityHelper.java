package com.itcook.cooking.api.global.security.jwt.helper;

import static com.itcook.cooking.api.global.consts.ItCookConstants.ACCESS_TOKEN_HEADER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.BEARER;
import static com.itcook.cooking.api.global.consts.ItCookConstants.REFRESH_TOKEN_HEADER;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcook.cooking.api.global.dto.ApiResponse;
import com.itcook.cooking.api.global.dto.ErrorResponse;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityHelper {


    public static void sendTokensSuccessResponse(ObjectMapper objectMapper,
        HttpServletResponse response,
        String message, String accessToken, String refreshToken)
        throws IOException {
        ApiResponse apiResponse = ApiResponse.OK(message);
        String body = objectMapper.writeValueAsString(apiResponse);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.addHeader(ACCESS_TOKEN_HEADER, BEARER + accessToken);
        response.addHeader(REFRESH_TOKEN_HEADER, BEARER + refreshToken);
        response.getWriter().write(body);
    }

    public static void sendLoginErrorResponse(ObjectMapper objectMapper,
        HttpServletResponse response) throws IOException {
        UserErrorCode errorCode = UserErrorCode.USER_NOT_FOUND;
        ErrorResponse errorResponse = ErrorResponse.ERROR(errorCode,
            "아이디 또는 비밀번호가 올바르지 않습니다.");

        String body = objectMapper.writeValueAsString(errorResponse);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(body);
    }

}
