package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.api.domains.user.service.dto.SendEmailServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Schema(name = "계정 찾기 요청")
public record FindUserRequest(
    @Schema(description = "이메일", example = "user@gmail.com")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    String email
) {

    public SendEmailServiceDto toServiceDto() {
        return SendEmailServiceDto.builder()
            .email(email)
            .build()
            ;
    }
}
