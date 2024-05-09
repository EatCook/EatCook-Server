package com.itcook.cooking.api.global.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.itcook.cooking.api.IntegrationRedisContainerSupport;
import com.itcook.cooking.api.global.security.jwt.config.RedisTestContainers;
import com.itcook.cooking.infra.redis.RedisService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

//@ActiveProfiles("test")
//@Import(RedisTestContainers.class)
//@SpringBootTest
@Disabled
public class RedisTest extends IntegrationRedisContainerSupport {

    @Autowired
    private RedisService redisService;

    @Test
    @DisplayName("Redis set 테스트")
    void test1() {
        //given
        String key = "testKey";
        String expectedValue = "testValue";

        //when
        redisService.setDataWithExpire(key, expectedValue,60L);

        //then
        String result = (String) redisService.getData(key);
        assertEquals(expectedValue, result);
    }

    @Test
    @DisplayName("Redis 저장 후 삭제 테스트")
    void test2() {
        //given
        String key = "testKey";
        String expectedValue = "testValue";
        //when
        redisService.setDataWithExpire(key, expectedValue, 60L);
        redisService.deleteData(key);
        //then
        Object result = redisService.getData(key);
        assertNull(result);
    }

    @Test
    @DisplayName("blackList 추가 테스트")
    void test3() {
        //given
        //when
        String testaccessToken = "testAccessToken";
        redisService.addBlackList(testaccessToken,3000L);

        //then
        String value = (String) redisService.getData(testaccessToken);

        assertEquals("logout", value);
    }

//    @Test
//    @DisplayName("Redis 저장 후 만료 테스트")
//    void test3() throws InterruptedException {
//        //given
//        String key = "testKey";
//        String expectedValue = "testValue";
//        //when
//        redisService.setDataWithExpire(key, expectedValue, 5L);
//        TimeUnit.SECONDS.sleep(6);
//
//        //then
//        Object result = redisService.getData(key);
//        assertNull(result);
//    }

}
