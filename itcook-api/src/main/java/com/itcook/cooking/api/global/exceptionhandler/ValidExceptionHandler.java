package com.itcook.cooking.api.global.exceptionhandler;

import com.itcook.cooking.api.global.dto.ErrorResponse;
import com.itcook.cooking.api.global.errorcode.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(1)
@RestControllerAdvice
public class ValidExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validException(
        MethodArgumentNotValidException exception
    ) {
        log.error("", exception);

        ErrorResponse errorResponse = ErrorResponse.ERROR(CommonErrorCode.BAD_REQUEST);

        for (FieldError fieldError : exception.getFieldErrors()) {
            errorResponse.addValidation(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(CommonErrorCode.BAD_REQUEST.getHttpStatusCode())
            .body(errorResponse)
            ;
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorResponse> nullPointException(NullPointerException exception) {
        log.error("", exception);

        ErrorResponse errorResponse = ErrorResponse.ERROR(CommonErrorCode.NULL_POINT);

        return ResponseEntity.status(CommonErrorCode.NULL_POINT.getHttpStatusCode())
            .body(errorResponse)
            ;
    }
}
