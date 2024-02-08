package com.itcook.cooking.api.global.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ApiResponse<T> {

    private final boolean success;
    private final String code;
    private final String message;
    private final T data;


    protected ApiResponse(boolean success, String code, String message, T data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> OK(T data) {
        return new ApiResponse<>(true, "200", "성공", data);
    }

    public static ApiResponse OK(String message) {
        return new ApiResponse(true, "200", message, null);
    }


    public static ApiResponse OK(String code, String message) {
        return new ApiResponse(true, code, message, null);
    }
}
