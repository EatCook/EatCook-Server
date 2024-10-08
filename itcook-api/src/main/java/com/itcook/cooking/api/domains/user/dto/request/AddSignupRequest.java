package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.api.domains.user.service.dto.AddSignupServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "추가 회원가입 요청")
public class AddSignupRequest {

    @Schema(description = "이메일", example = "user@gmail.com")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @Schema(description = "프로필 이미지 확장자명(필수값 X)", example = "jpg", requiredMode  = RequiredMode.NOT_REQUIRED)
    private String fileExtension;

    @Pattern(regexp = "^[가-힣]{2,6}$", message = "2~6자의 한글 닉네임(특수문자 사용 불가)")
    @NotBlank(message = "닉네임을 입력해주세요")
    @Schema(description = "닉네임", example = "잇쿡")
    private String nickName;

    @Size(max = 3, message = "최대 3개까지 선택 가능합니다.")
    @Schema(description = "요리 유형", example = "[\"한식\", \"중식\", \"일식\"]")
    private List<String> cookingType;

    @Schema(description = "생활 유형", example = "다이어트만 n년째")
    private String lifeType;

    public AddSignupServiceDto toServiceDto() {
        return AddSignupServiceDto.builder()
            .email(email)
            .fileExtension(fileExtension)
            .nickName(nickName)
            .cookingType(cookingType)
            .lifeType(lifeType)
            .build()
            ;
    }

}
