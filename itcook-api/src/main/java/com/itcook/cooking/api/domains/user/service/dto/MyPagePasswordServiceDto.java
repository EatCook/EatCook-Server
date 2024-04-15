package com.itcook.cooking.api.domains.user.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MyPagePasswordServiceDto {

    private String email;
    private String currentPassword;
    private String newPassword;

}
