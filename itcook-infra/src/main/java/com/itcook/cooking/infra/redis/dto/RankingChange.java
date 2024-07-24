package com.itcook.cooking.infra.redis.dto;

import com.itcook.cooking.domain.domains.infra.redis.dto.RankingWords;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RankingChange {

    private String word;
    private long score;
    private int rank;
    private int rankChange;

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
            .searchCount(score)
            .rank(rank)
            .rankChange(rankChange)
            .build()
            ;
    }


}
