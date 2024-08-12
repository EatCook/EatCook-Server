package com.itcook.cooking.api.domains.user.dto.request;

import static com.itcook.cooking.domain.common.constant.UserConstant.PASSWORD_REGEXP;

import com.itcook.cooking.api.domains.user.service.dto.MyPagePasswordServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Schema(name = "마이페이지 비밀번호 변경 요청")
public class MyPageChangePasswordRequest {

    @NotBlank(message = "현재 비밀번호를 입력하지 않았습니다.")
    @Schema(description = "현재 비밀번호", example = "current123")
    private String currentPassword;

    @NotBlank(message = "새로운 비밀번호를 입력하지 않았습니다")
    @Pattern(message = "패스워드는 8자리 이상이어야 하며, 영문과 숫자를 포함해야 합니다.", regexp = PASSWORD_REGEXP)
    @Schema(description = "새로운 비밀번호", example = "new12345")
    private String newPassword;

    @Builder
    private MyPageChangePasswordRequest(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }

    public MyPagePasswordServiceDto toServiceDto(String email) {
        return MyPagePasswordServiceDto.builder()
            .email(email)
            .rawCurrentPassword(currentPassword)
            .newPassword(currentPassword)
            .build();
    }
}
