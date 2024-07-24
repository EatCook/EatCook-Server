package com.itcook.cooking.api.domains.user.dto.request;

import static com.itcook.cooking.domain.common.constant.UserConstant.PASSWORD_REGEXP;

import com.itcook.cooking.api.domains.user.service.dto.FindUserChangePasswordServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Builder;

@Builder
public record FindUserNewPasswordRequest(
    @Schema(description = "이메일", example = "user@gmail.com")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    String email,
    @Schema(description = "비밀번호", example = "cook1234")
    @Pattern(message = "패스워드는 8자리 이상이어야 하며, 영문과 숫자를 포함해야 합니다.", regexp = PASSWORD_REGEXP)
    @NotBlank(message = "새비밀번호를 입력해주세요.")
    String password
) {

    public FindUserChangePasswordServiceDto toServiceDto() {
        return FindUserChangePasswordServiceDto.builder()
            .email(email)
            .password(password)
            .build();
    }
}
