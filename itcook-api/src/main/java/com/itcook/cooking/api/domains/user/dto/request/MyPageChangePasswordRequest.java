package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.api.domains.user.service.dto.MyPagePasswordServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.NotBlank;
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
            .currentPassword(currentPassword)
            .newPassword(currentPassword)
            .build();
    }
}
