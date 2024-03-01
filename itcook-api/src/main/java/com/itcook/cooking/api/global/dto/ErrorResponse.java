package com.itcook.cooking.api.global.dto;

import com.itcook.cooking.domain.common.errorcode.ErrorCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter @Setter
public class ErrorResponse extends ApiResponse{

    @Schema(description = "에러 validation 필드",requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    private final Map<String, String> validation;

    protected ErrorResponse(String code, String message) {
        super(false, code, message, null);
        this.validation = new ConcurrentHashMap<>();
    }

    protected ErrorResponse(ErrorCode errorCode) {
        super(false, errorCode.getErrorCode(), errorCode.getDescription(), null);
        this.validation = new ConcurrentHashMap<>();
    }

    public static ErrorResponse ERROR(String code, String message) {
        return new ErrorResponse(code, message);
    }

    public static ErrorResponse ERROR(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse ERROR(ErrorCode errorCode, String message) {
        return new ErrorResponse(errorCode.getErrorCode(), message);
    }

    public void addValidation(String fieldName, String errorMessage) {
        validation.put(fieldName, errorMessage);

    }
}
