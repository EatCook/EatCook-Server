package com.itcook.cooking.infra.redis.event;

import com.itcook.cooking.infra.redis.RedisService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SearchWordsEventListener {

    private final RedisService redisService;

    @Async
    @EventListener
    public void handle(RealTimeSearchWords realTimeSearchWords) {
        log.info("SearchWordsEventListener Start");
        List<String> searchWords = realTimeSearchWords.getSearchWords();
        searchWords.forEach(word -> {
            redisService.incrementScore("searchWords", word, 1L);
        });
    }

}
