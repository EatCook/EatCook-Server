package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.domain.domains.user.service.dto.MyPageUpdateProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import lombok.Builder;

@Schema(name = "마이페이지 프로필 변경 요청")
@Builder
public record MyPageUpdateProfileRequest(
    @NotBlank(message = "닉네임을 입력해야합니다.")
    @Schema(description = "변경할 닉네임", example = "잇쿡 유저")
    String nickName
) {

    public MyPageUpdateProfile toServiceDto(String email) {
        return MyPageUpdateProfile.builder()
            .email(email)
            .nickName(nickName)
            .build();
    }
}
