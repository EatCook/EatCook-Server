package com.itcook.cooking.api.global.mock;

import com.itcook.cooking.domain.domains.infra.redis.RedisService;
import com.itcook.cooking.domain.domains.infra.redis.dto.RankingWords;
import java.util.List;

public class MockRedisService implements RedisService {

    @Override
    public void setDataWithExpire(String key, Object value, Long expireSeconds) {

    }

    @Override
    public Object getData(String key) {
        return "refreshToken";
    }

    @Override
    public void deleteData(String key) {

    }

    @Override
    public void deleteKeysContaining(String pattern) {

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
