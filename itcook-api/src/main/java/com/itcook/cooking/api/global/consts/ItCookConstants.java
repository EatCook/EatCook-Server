package com.itcook.cooking.api.global.consts;

public class ItCookConstants {

    public static final String ACCESS_TOKEN_SUBJECT = "access-token";
    public static final String REFRESH_TOKEN_SUBJECT = "refresh-token";
    public static final String USERNAME_CLAIM = "username";
    public static final String ROLES_CLAIM = "authorities";
    public static final String BEARER = "Bearer ";
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Authorization-refresh";

    public static final String[] SWAGGER_PATTERNS = {
        "/swagger-ui/**","/swagger-resources/**","/swagger-ui.html","/v3/api-docs",
        "/v3/api-docs/**","/webjars/**"
    };

}
