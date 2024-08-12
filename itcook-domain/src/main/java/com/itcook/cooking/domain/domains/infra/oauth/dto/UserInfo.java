package com.itcook.cooking.domain.domains.infra.oauth.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserInfo {

    private String email;
    private String nickName;

    @Builder
    public UserInfo(String email, String nickName) {
        this.email = email;
        this.nickName = nickName;
    }

}
