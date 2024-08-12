package com.itcook.cooking.infra.oauth.apple;

import com.itcook.cooking.domain.domains.infra.oauth.dto.UserInfo;
import lombok.Getter;

@Getter
public class AppleIdTokenPayload {
    private String sub;
    private String email;

    public UserInfo of() {
        return UserInfo.builder()
            .email(email)
            .nickName(sub)
            .build()
            ;
    }
}
