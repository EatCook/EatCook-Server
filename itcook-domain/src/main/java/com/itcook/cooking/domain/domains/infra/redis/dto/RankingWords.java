package com.itcook.cooking.domain.domains.infra.redis.dto;

import lombok.Builder;

@Builder
public record RankingWords(
    String word,
    long searchCount,
    long rank,
    int rankChange
) {

}
