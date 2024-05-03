package com.itcook.cooking.api.domains.user.dto.request;

import static com.itcook.cooking.domain.common.constant.UserConstant.EMAIL_REGEXP;

import com.itcook.cooking.api.domains.user.service.dto.SendEmailServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "이메일 인증 요청")
public class SendEmailAuthRequest {

    @Schema(description = "이메일", example = "user@gmail.com")
    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    public SendEmailServiceDto toServiceDto() {
        return SendEmailServiceDto.builder()
            .email(email)
            .build()
            ;
    }
}
