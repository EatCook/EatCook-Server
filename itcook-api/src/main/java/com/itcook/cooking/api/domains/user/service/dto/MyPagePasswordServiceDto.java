package com.itcook.cooking.api.domains.user.service.dto;

import com.itcook.cooking.domain.domains.user.service.dto.UserUpdatePassword;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyPagePasswordServiceDto {

    private String email;
    private String rawCurrentPassword;
    private String newPassword;

    public UserUpdatePassword toDomainService() {
        return UserUpdatePassword.builder()
            .email(email)
            .rawCurrentPassword(rawCurrentPassword)
            .newPassword(newPassword)
            .build()
            ;
    }

}
