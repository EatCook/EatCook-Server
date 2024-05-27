package com.itcook.cooking.domain.domains.mock;

import com.itcook.cooking.domain.infra.redis.RedisService;
import com.itcook.cooking.domain.infra.redis.dto.RankingWords;
import java.util.List;

public class MockRedisService implements RedisService {

    @Override
    public void setDataWithExpire(String key, Object value, Long expireSeconds) {

    }

    @Override
    public Object getData(String key) {
        return null;
    }

    @Override
    public void deleteData(String key) {

    }

    @Override
    public void addBlackList(String accessToken, long time) {

    }

    @Override
    public void incrementScore(String key, String value, long score) {

    }

    @Override
    public List<RankingWords> getRankingWords() {
        return null;
    }
}
