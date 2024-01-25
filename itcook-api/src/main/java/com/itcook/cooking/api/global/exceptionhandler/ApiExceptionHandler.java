package com.itcook.cooking.api.global.exceptionhandler;

import com.itcook.cooking.api.global.dto.ErrorResponse;
import com.itcook.cooking.api.global.errorcode.ErrorCode;
import com.itcook.cooking.api.global.exception.ApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(1)
@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> apiException(ApiException apiException) {
        log.info("",apiException);

        ErrorCode errorCode = apiException.getErrorCode();
        String errorDescription = apiException.getErrorDescription();

        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ErrorResponse.ERROR(errorCode, errorDescription))
                ;

    }
}
