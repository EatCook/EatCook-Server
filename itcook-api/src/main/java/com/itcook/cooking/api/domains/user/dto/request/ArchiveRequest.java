package com.itcook.cooking.api.domains.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "보관함 요청")
public class ArchiveRequest {

    @NotNull
    @Schema(description = "postId", example = "1")
    private Long postId;

}
