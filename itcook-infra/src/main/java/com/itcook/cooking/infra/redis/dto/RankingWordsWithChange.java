package com.itcook.cooking.infra.redis.dto;

import com.itcook.cooking.domain.domains.infra.redis.dto.RankingWords;
import lombok.Builder;

@Builder
public record RankingWordsWithChange(
    String word,
    long score,
    long rank,
    int rankChange
) {

    public RankingWords of() {
        return RankingWords.builder()
            .rank(rank)
            .score(score)
            .rankChange(rankChange)
            .word(word)
            .build()
            ;
    }
}
