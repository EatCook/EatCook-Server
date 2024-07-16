package com.itcook.cooking.infra.feign.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class AppleTokenRequest {

    private String code;
    private String clientId;
    private String clientSecret;
    private String grantType;

}
