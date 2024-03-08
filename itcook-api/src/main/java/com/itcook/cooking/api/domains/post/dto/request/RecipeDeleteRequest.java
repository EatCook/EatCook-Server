package com.itcook.cooking.api.domains.post.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "recipe delete request")
public class RecipeDeleteRequest {

    @Schema(description = "email", example = "user@test.com")
    @NotNull(message = "이메일을 입력해 주세요")
    private String email;

    @Schema(description = "postId", example = "1")
    @NotNull(message = "게시글 번호가 없습니다.")
    private Long postId;
}
