package com.itcook.cooking.infra.feign.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class AppleSocialTokenInfoResponse {

    private String accessToken;
    private String tokenType;
    private Long expiresIn;
    private String refreshToken;
    private String idToken;
}
