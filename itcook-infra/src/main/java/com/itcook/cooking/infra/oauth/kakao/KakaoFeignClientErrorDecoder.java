package com.itcook.cooking.infra.oauth.kakao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcook.cooking.domain.common.errorcode.CommonErrorCode;
import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.infra.oauth.dto.KakaoErrorResponse;
import feign.FeignException;
import feign.Response;
import feign.Util;
import feign.codec.ErrorDecoder;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class KakaoFeignClientErrorDecoder implements ErrorDecoder {


    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            String responseBody = Util.toString(response.body().asReader(StandardCharsets.UTF_8));
            log.error("Feign Response Body: {}", responseBody);

            ObjectMapper objectMapper = new ObjectMapper();
            KakaoErrorResponse kakaoErrorResponse = objectMapper.readValue(responseBody, KakaoErrorResponse.class);

            switch (kakaoErrorResponse.code()) {
                case "-1":
                    throw new ApiException(CommonErrorCode.BAD_REQUEST, "카카오 서비스 장애");
                case "-2":
                    throw new ApiException(CommonErrorCode.BAD_REQUEST, "요청 파라미터(필수 인자나 인자 데이터 타입이 적절치 않음)를 확인해주세요.");
                case "-401":
                    throw new ApiException(UserErrorCode.TOKEN_NOT_VALID);
                default:
                    throw new ApiException(CommonErrorCode.BAD_REQUEST, kakaoErrorResponse.msg());
            }
        } catch (IOException e) {
            log.error("카카오 Feign Response 디코딩 에러 : ", e);
        }
        return FeignException.errorStatus(methodKey, response);
    }
}
