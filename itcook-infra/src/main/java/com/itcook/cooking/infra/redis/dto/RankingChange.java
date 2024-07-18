package com.itcook.cooking.infra.redis.dto;

import com.itcook.cooking.domain.domains.infra.redis.dto.RankingWords;
import lombok.Builder;

@Builder
public record RankingChange(
    String word,
    long score,
    int rank,
    int rankChange
) {

    public static RankingChange of(String word, long score,int rank, int rankChange) {
        return RankingChange.builder()
            .word(word)
            .score(score)
            .rank(rank)
            .rankChange(rankChange)
            .build();
    }

    public RankingWords toRankingWords() {
        return RankingWords.builder()
            .word(word)
            .score(score)
            .rank(rank)
            .rankChange(rankChange)
            .build()
            ;
    }
}
