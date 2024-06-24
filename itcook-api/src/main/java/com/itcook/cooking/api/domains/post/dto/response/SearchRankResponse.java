package com.itcook.cooking.api.domains.post.dto.response;

import com.itcook.cooking.domain.domains.infra.redis.dto.RankingWords;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
//@Schema(name = "검색어 랭킹 응답")
public class SearchRankResponse {

//    @Schema(description = "검색어", example = "김치찌개")
    private String searchWord;
//    @Schema(description = "검색 횟수", example = "10")
    private Long searchCount;

    public static SearchRankResponse of(RankingWords rankingWords) {
        return SearchRankResponse.builder()
            .searchWord(rankingWords.searchWord())
            .searchCount(rankingWords.searchCount())
            .build();
    }
}
