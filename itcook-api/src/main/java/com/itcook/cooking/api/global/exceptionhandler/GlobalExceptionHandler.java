package com.itcook.cooking.api.global.exceptionhandler;

import com.itcook.cooking.api.global.dto.ErrorResponse;
import com.itcook.cooking.api.global.errorcode.CommonErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception exception) {
        log.error("", exception);

        ErrorResponse errorResponse = ErrorResponse.ERROR(CommonErrorCode.SERVER_ERROR);
        return ResponseEntity.status(CommonErrorCode.SERVER_ERROR.getHttpStatusCode())
            .body(errorResponse)
            ;
    }
}
