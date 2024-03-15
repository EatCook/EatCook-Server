package com.itcook.cooking.infra.redis;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;


    public void setDataWithExpire(String key, Object value, Long expireSeconds) {
        redisTemplate.opsForValue().set(key, value, expireSeconds, TimeUnit.SECONDS);
    }

    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
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

    public Set<TypedTuple<Object>> getRankingWords() {
        return redisTemplate.opsForZSet().reverseRangeWithScores("searchWords", 0, 9);
    }

}
