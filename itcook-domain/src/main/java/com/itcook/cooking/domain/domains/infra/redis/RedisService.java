package com.itcook.cooking.domain.domains.infra.redis;

import com.itcook.cooking.domain.domains.infra.redis.dto.RankingWords;
import java.util.List;

public interface RedisService {

    void setDataWithExpire(String key, Object value, Long expireSeconds);

    Object getData(String key);

    void deleteData(String key);

    void deleteKeysContaining(String pattern);

    /**
     * 로그아웃시 access token 블랙 리스트 추가 메소드
     */
    void addBlackList(String accessToken, long time);

    void incrementScore(String key, String value, long score);

    List<RankingWords> getRankingWords();
}
