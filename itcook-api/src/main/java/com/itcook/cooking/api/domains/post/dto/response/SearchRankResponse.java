package com.itcook.cooking.api.domains.post.dto.response;

import com.itcook.cooking.domain.domains.infra.redis.dto.RankingWords;
import com.itcook.cooking.domain.domains.infra.redis.dto.WordsRanking;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(name = "검색어 랭킹 응답")
public class SearchRankResponse {

    private String lastUpdateTime;
    private List<Ranking> rankings;

    public static SearchRankResponse from(WordsRanking wordsRanking) {
        return SearchRankResponse.builder()
            .lastUpdateTime(wordsRanking.lastUpdateTime())
            .rankings(wordsRanking.rankingWords().stream().map(Ranking::of).toList())
            .build()
            ;
    }


    @Getter
    @Builder
    public static class Ranking {
        @Schema(description = "검색어")
        private String searchWord;
        @Schema(description = "검색 횟수")
        private Long searchCount;
        @Schema(description = "현재 등수")
        private Long rank;
        @Schema(description = "등수 변화값. 등수가 변화하지 않았거나 새로 들어온 검색어는 default로 0을 가진다.", example = "2 or 0 or -2")
        private int rankChange;

        static Ranking of(RankingWords rankingWords) {
            return Ranking.builder()
                .searchWord(rankingWords.word())
                .searchCount(rankingWords.searchCount())
                .rank(rankingWords.rank())
                .rankChange(rankingWords.rankChange())
                .build();
        }
    }

}
