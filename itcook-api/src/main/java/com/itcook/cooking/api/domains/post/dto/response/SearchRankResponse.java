package com.itcook.cooking.api.domains.post.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "검색어 랭킹 응답")
public class SearchRankResponse {

    @Schema(description = "검색어", example = "김치찌개")
    private String searchWord;
    @Schema(description = "검색 횟수", example = "10")
    private Long searchCount;

    public static SearchRankResponse of(TypedTuple<Object> typedTuple) {
        return SearchRankResponse.builder()
            .searchWord(String.valueOf(typedTuple.getValue()))
            .searchCount(typedTuple.getScore().longValue())
            .build();
    }
}
