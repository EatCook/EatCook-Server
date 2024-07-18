package com.itcook.cooking.domain.domains.infra.redis.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record WordsRanking(
    String lastUpdateTime,
    List<RankingWords> rankingWords
) {

    public static WordsRanking of(String lastUpdateTime, List<RankingWords> rankingWords) {
        return WordsRanking.builder()
            .lastUpdateTime(lastUpdateTime)
            .rankingWords(rankingWords)
            .build()
            ;
    }
}
