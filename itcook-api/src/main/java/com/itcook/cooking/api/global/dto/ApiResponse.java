package com.itcook.cooking.api.global.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Schema(description = "응답 공통 Response Dto")
public class ApiResponse<T> {

    @Schema(description = "성공 여부 입니다.", example = "true")
    private final boolean success;
    @Schema(description = "응답 코드 입니다.", example = "200")
    private final String code;
    @Schema(title = "응답 메시지", example = "성공")
    private final String message;
    @Schema(title = "응답 데이터")
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
