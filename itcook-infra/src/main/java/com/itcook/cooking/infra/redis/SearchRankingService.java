package com.itcook.cooking.infra.redis;

import com.itcook.cooking.infra.redis.dto.RankingChange;
import com.itcook.cooking.infra.redis.dto.RankingWords;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchRankingService {

    private final RedisTemplate<String, Object> redisTemplate;
    private List<RankingChange> cachedRankingChanges;
    private LocalDateTime lastUpdateTime;

    public List<RankingWords> getRankingWords(String key) {
        Set<TypedTuple<Object>> searchWords = redisTemplate.opsForZSet()
            .reverseRangeWithScores(key, 0, 9);

        if (CollectionUtils.isEmpty(searchWords)) {
            return List.of();
        }

        return searchWords.stream().map(word -> RankingWords.of(
                String.valueOf(word.getValue()),
                Double.valueOf(word.getScore()).longValue()))
            .collect(Collectors.toList());
    }

    public List<RankingChange> getRankingChanges() {
//        List<RankingWords> previousRankingWords = getRankingWords("previousSearchWords");
//        List<RankingWords> currentRankingWords = getRankingWords("searchWords");
//
//        redisTemplate.delete("previousSearchWords");
//        currentRankingWords.forEach(word ->
//            redisTemplate.opsForZSet().add("previousSearchWords", word.word(), word.searchCount()));
//
//        return calculateRankingChanges(previousRankingWords, currentRankingWords);
        if (cachedRankingChanges == null) {
            updateRankingChanges(); // 최초 실행 시 또는 캐시가 비어있을 때 업데이트
        }
        return cachedRankingChanges;
    }

    private List<RankingChange> calculateRankingChanges(List<RankingWords> previous, List<RankingWords> current) {
        Map<String, Integer> previousRankingMap = IntStream.range(0, previous.size())
            .boxed()
            .collect(Collectors.toMap(i -> previous.get(i).word(), i -> i + 1));

        return current.stream().map(currentWord -> {
            Integer previousRank = previousRankingMap.get(currentWord.word());
            int currentRank = current.indexOf(currentWord) + 1;
            int rankChange = (previousRank == null) ? 0 : previousRank - currentRank;

            return RankingChange.of(currentWord.word(), currentWord.score(),currentRank, rankChange);
        }).collect(Collectors.toList());
    }

//    @Scheduled(cron = "0 0 * * * *")
//    @Scheduled(fixedRate = 30000) // 30초
    public void updateRankingChanges() {
        List<RankingWords> previousRankingWords = getRankingWords("previousSearchWords");
        List<RankingWords> currentRankingWords = getRankingWords("searchWords");

        redisTemplate.delete("previousSearchWords");
        currentRankingWords.forEach(word ->
            redisTemplate.opsForZSet().add("previousSearchWords", word.word(), word.score()));

        cachedRankingChanges = calculateRankingChanges(previousRankingWords, currentRankingWords);
        lastUpdateTime = LocalDateTime.now();
        log.info("검색어 랭킹 업데이트 {}", lastUpdateTime);
    }

//    public LocalDateTime getLastUpdateTime() {
//        return lastUpdateTime;
//    }
}

