package com.itcook.cooking.infra.oauth.dto;

public record KakaoErrorResponse(
    String msg,
    String code
) {

}
