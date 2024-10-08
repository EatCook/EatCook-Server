package com.itcook.cooking.api.domains.post.dto.request;

import com.itcook.cooking.api.domains.post.service.dto.PostSearchServiceDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "게시글 검색 요청")
public class PostSearchRequest {

    @Schema(description = "검색 결과를 가져올 마지막 게시글의 id (첫번째 검색 null 허용)", example = "10")
    private Long lastId;
    @Schema(description = "검색할 레시피명", example = "[ \"김치찌개\" ]")
    private List<String> recipeNames;
    @Schema(description = "검색할 재료명", example = "[ \"김치\" ]")
    private List<String> ingredients;
    @Schema(description = "가져올 게시글의 개수, 기본 사이즈 = 10", example = "10")
    @Builder.Default
    private Integer size = 10;

    public PostSearchServiceDto toServiceDto() {
        return PostSearchServiceDto.builder()
            .lastId(lastId)
            .recipeNames(recipeNames)
            .ingredients(ingredients)
            .size(size)
            .build();
    }
}
