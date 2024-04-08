package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.api.domains.user.service.dto.MyPagePasswordServiceDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MyPageChangePasswordRequest {

    private String email;
    private String currentPassword;
    private String newPassword;

    @Builder
    private MyPageChangePasswordRequest(String email, String currentPassword, String newPassword) {
        this.email = email;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public MyPagePasswordServiceDto toServiceDto(String email) {
        return MyPagePasswordServiceDto.builder()
            .email(email)
            .currentPassword(currentPassword)
            .newPassword(currentPassword)
            .build();
    }
}
