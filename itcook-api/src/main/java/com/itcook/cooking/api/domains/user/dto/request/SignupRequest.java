package com.itcook.cooking.api.domains.user.dto.request;

import static com.itcook.cooking.domain.common.constant.UserConstant.EMAIL_REGEXP;
import static com.itcook.cooking.domain.common.constant.UserConstant.PASSWORD_REGEXP;

import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
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
@Schema(name = "회원가입")
public class SignupRequest {

    @Schema(description = "이메일", example = "user@gmail.com")
//    @Email(message = "이메일 형식에 맞게 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;
    @Schema(description = "비밀번호", example = "hahahaha4ha12")
//    @Pattern(message = "패스워드는 8자리 이상이어야 하며, 영문과 숫자를 포함해야 합니다.", regexp = PASSWORD_REGEXP)
    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;

    public ItCookUser toDomain() {
        return ItCookUser.builder()
            .email(email)
            .password(password)
            .providerType(ProviderType.COMMON)
            .userRole(UserRole.USER)
            .build();
    }

}
