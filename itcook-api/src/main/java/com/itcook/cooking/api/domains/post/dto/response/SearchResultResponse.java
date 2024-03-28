package com.itcook.cooking.api.domains.post.dto.response;

import com.itcook.cooking.api.domains.post.dto.search.SearchPostResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "검색 결과 응답")
public class SearchResultResponse {
    @Schema(description = "재료 or 레시피명")
    private String name;
    @Schema(description = "검색 결과")
    private List<SearchPostResponse> searchResults;

}
