package com.itcook.cooking.api.domains.user.dto.request;

import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.LifeType;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @NotNull
    @Schema(description = "유저 id", example = "1")
    private Long userId;
    @Schema(description = "프로필 이미지 확장자명(필수값 X)", example = "jpg", requiredMode  = RequiredMode.NOT_REQUIRED)
    private String fileExtension;
    @Pattern(regexp = "^[가-힣]{2,6}$", message = "2~6자의 한글 닉네임(특수문자 사용 불가)")
    @NotBlank(message = "닉네임을 입력해주세요")
    @Schema(description = "닉네임", example = "잇쿡")
    private String nickName;
    @Size(max = 3, message = "최대 3개까지 선택 가능합니다.")
    @Schema(description = "요리 유형", example = "[\"한식\", \"중식\", \"일식\"]")
    private List<String> cookingType;
    // TODO 넘어가기?
    @NotEmpty(message = "생활 유형을 선택해주세요")
    @Schema(description = "생활 유형", example = "다이어트만 n번째")
    private String lifeType;

    public List<CookingType> toCookingTypes() {
        return this.cookingType.stream()
            .map(CookingType::getByName)
            .toList();
    }

    public ItCookUser toEntity() {
        return ItCookUser.builder()
            .id(this.userId)
            .nickName(this.nickName)
            .lifeType(LifeType.getByName(this.lifeType))
            .build();
    }

}
