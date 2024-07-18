package com.itcook.cooking.domain.domains.infra.redis.dto;

import lombok.Builder;

@Builder
public record RankingWords(
    String word,
    long score,
    long rank,
    int rankChange
) {

}
