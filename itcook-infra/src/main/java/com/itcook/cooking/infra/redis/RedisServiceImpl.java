package com.itcook.cooking.infra.redis;

import com.itcook.cooking.domain.domains.infra.redis.RedisService;
import com.itcook.cooking.domain.domains.infra.redis.dto.WordsRanking;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final SearchRankingService searchRankingService;
    private final SearchRankingV2Service searchRankingV2Service;

    public void setDataWithExpire(String key, Object value, Long expireSeconds) {
        redisTemplate.opsForValue().set(key, value, expireSeconds, TimeUnit.SECONDS);
    }

    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

    public void deleteKeysContaining(String pattern) {
        Set<String> keys = redisTemplate.keys("*" + pattern + "*");
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.unlink(keys); // 비동기로 삭제
        }
    }

    /**
     * 로그아웃시 access token 블랙 리스트 추가 메소드
     */
    public void addBlackList(String accessToken, long time) {
        redisTemplate.opsForValue()
            .set(accessToken, "logout", time, TimeUnit.MILLISECONDS);
    }

    public void incrementScore(String key, String value, long score) {
        redisTemplate.opsForZSet().incrementScore(key, value, score);
    }

    public WordsRanking getRankingWords() {
//        List<RankingChange> rankingChanges = searchRankingService.getRankingChanges();
//        LocalDateTime lastUpdateTime = searchRankingService.getLastUpdateTime();
        return searchRankingV2Service.getTopSearchWords();
    }

    @Override
    public void updateRankingChanges() {
        searchRankingV2Service.updateRanking();
    }


}
