package com.itcook.cooking.api.domains.user.dto.request;

import static com.itcook.cooking.domain.common.constant.UserConstant.EMAIL_REGEXP;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "이메일 인증 요청")
public class SendEmailAuthRequest {

    @Schema(description = "이메일", example = "user@gmail.com")
    @Pattern(message = "이메일 형식에 맞게 입력해주세요.", regexp = EMAIL_REGEXP)
    private String email;

//
//    public EmailRequest toServiceRequest() {
//        return EmailRequest.builder()
//            .email(this.email)
//            .build();
//    }
}
