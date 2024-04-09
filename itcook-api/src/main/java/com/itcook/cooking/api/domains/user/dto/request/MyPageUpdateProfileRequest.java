package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.domain.domains.user.service.dto.MyPageUpdateProfile;
import javax.validation.constraints.NotBlank;

public record MyPageUpdateProfileRequest(
    @NotBlank(message = "닉네임을 입력해야합니다.")
    String nickName
) {

    public MyPageUpdateProfile toServiceDto(String email) {
        return MyPageUpdateProfile.builder()
            .email(email)
            .nickName(nickName)
            .build();
    }
}
