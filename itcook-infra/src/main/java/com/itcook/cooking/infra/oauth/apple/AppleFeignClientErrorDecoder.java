package com.itcook.cooking.infra.oauth.apple;

import com.itcook.cooking.domain.common.errorcode.CommonErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class AppleFeignClientErrorDecoder implements ErrorDecoder {


    @Override
    public Exception decode(String methodKey, Response response) {
        String bodyString = null;
        if (response.body() != null) {
            try {
                bodyString = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            } catch (IOException e) {
                log.error("애플 Feign Response 디코딩 에러 : ", e);
            }
        }
        log.error("애플 소셜 로그인 Feign API Feign Client 호출 중 오류가 발생되었습니다. body: {}", bodyString);

        return new ApiException(CommonErrorCode.BAD_REQUEST, "애플 소셜 로그인 Feign API Feign Client 호출 오류");
    }
}
