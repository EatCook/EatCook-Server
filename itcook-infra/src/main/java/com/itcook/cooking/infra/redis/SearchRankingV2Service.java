package com.itcook.cooking.infra.redis;

import static com.itcook.cooking.domain.common.constant.SearchKeyConstant.CACHED_RANKING_KEY;
import static com.itcook.cooking.domain.common.constant.SearchKeyConstant.LAST_UPDATE_KEY;
import static com.itcook.cooking.domain.common.constant.SearchKeyConstant.SEARCH_RANKING_KEY;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itcook.cooking.domain.common.constant.SearchKeyConstant;
import com.itcook.cooking.domain.domains.infra.redis.dto.WordsRanking;
import com.itcook.cooking.infra.redis.dto.RankingChange;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchRankingV2Service {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(cron = "0 0 * * * *")
//    @Scheduled(fixedRate = 50000) // 30초
    public void updateRanking() {
        log.info("검색어 랭킹 업데이트");
        Set<TypedTuple<Object>> currentRanking = redisTemplate.opsForZSet()
            .reverseRangeWithScores(SEARCH_RANKING_KEY, 0, 9);
        List<RankingChange> result = new ArrayList<>();

        String previousRankingJson = (String) redisTemplate.opsForValue().get(CACHED_RANKING_KEY);
        Map<String, Integer> previousRanks = new HashMap<>();
        if (previousRankingJson != null) {
            try {
                List<RankingChange> previousRanking = objectMapper.readValue(previousRankingJson, new TypeReference<List<RankingChange>>() {});
                for (RankingChange term : previousRanking) {
                    previousRanks.put(term.getWord(), term.getRank());
                }
            } catch (JsonProcessingException e) {
                // 에러 처리
                log.error("이전 랭킹 json 파싱 에러", e);
            }
        }

        int rank = 1;
        log.info("현재 검색어 사이즈 : {}", currentRanking.size());
        for (TypedTuple<Object> term : currentRanking) {
            String word = String.valueOf(term.getValue());
            Double score = redisTemplate.opsForZSet().score(SEARCH_RANKING_KEY, word); // 조회수

            RankingChange searchTerm = RankingChange.builder()
                .word(word)
                .score(score.longValue()) // 조회수
                .rank(rank)
                .build();

            Integer previousRank = previousRanks.get(word);
            if (previousRank != null) {
                int rankChange = previousRank - rank;
                searchTerm.setRankChange(rankChange);
            } else {
                searchTerm.setRankChange(currentRanking.size() - rank);
            }

            result.add(searchTerm);
            rank++;
        }

        try {
            redisTemplate.opsForValue().set(CACHED_RANKING_KEY, objectMapper.writeValueAsString(result));
        } catch (JsonProcessingException e) {
            log.error("cached_ranking_list json 파싱 에러", e);
        }
        redisTemplate.opsForValue().set(LAST_UPDATE_KEY, LocalDateTime.now().toString());
    }

    public WordsRanking getTopSearchWords() {
        String cachedRankingJson = (String) redisTemplate.opsForValue().get(CACHED_RANKING_KEY);
        List<RankingChange> rankings;

        try {
            rankings = cachedRankingJson != null ?
                objectMapper.readValue(cachedRankingJson, new TypeReference<List<RankingChange>>(){}) :
                new ArrayList<>();
        } catch (JsonProcessingException e) {
            log.error("getTopSearchWords의 cachedRankingJson 에러");
            rankings = new ArrayList<>();
        }

        return WordsRanking.of(getLastUpdateTime(), rankings.stream().map(RankingChange::toRankingWords)
            .toList());
    }

    private String getLastUpdateTime() {
        String lastUpdateStr = (String) redisTemplate.opsForValue().get(LAST_UPDATE_KEY);
        LocalDateTime lastUpdateTime = lastUpdateStr != null ? LocalDateTime.parse(lastUpdateStr) : LocalDateTime.now();
        return lastUpdateTime.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:00"));
    }
}
