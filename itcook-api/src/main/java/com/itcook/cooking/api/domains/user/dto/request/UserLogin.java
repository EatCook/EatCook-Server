package com.itcook.cooking.api.domains.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserLogin {

    private String email;
    private String password;
    private String deviceToken;

}
