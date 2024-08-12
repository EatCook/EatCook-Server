package com.itcook.cooking.infra.feign.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.itcook.cooking.domain.domains.infra.oauth.dto.UserInfo;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(SnakeCaseStrategy.class)
public class KakaoUserInfo {

    private Properties properties;
    private KakaoAccount kakaoAccount;

    @Builder
    public KakaoUserInfo(Properties properties, KakaoAccount kakaoAccount) {
        this.properties = properties;
        this.kakaoAccount = kakaoAccount;
    }

    public UserInfo of() {
        return UserInfo.builder()
            .email(kakaoAccount.getEmail())
            .nickName(properties.getNickname())
            .build();
    }

    @Getter
    @NoArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    static class Properties {
        private String nickname;
    }

    @Getter
    @NoArgsConstructor
    @JsonNaming(SnakeCaseStrategy.class)
    static class KakaoAccount {
        private String email;
    }

}
