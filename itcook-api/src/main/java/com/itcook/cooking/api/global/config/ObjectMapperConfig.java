package com.itcook.cooking.api.global.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

//    @Bean
//    public ObjectMapper objectMapper() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.registerModule(new Jdk8Module()); // JDK 8 version 이후
//        objectMapper.registerModule(new JavaTimeModule());
//        //Deserialization(역직렬화)할 때 Object와 매핑되지 않는 Json 데이터가 존재하는 경우 무시하는 설정이다.
//        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); // 모르는 json field를 무시한다. 빼고 파싱
//        //반대로 Serialization(직렬화)할 때 필드가 없는 Object인 경우 무시하는 설정이다.
//        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
//
//        // 날짜 관련 직렬화 LocalDate 타입을 직렬화할 때 ISO 8601 형식으로 하는 설정이다.
//        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//        // 스네이크 케이스
//        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
//
//        return objectMapper;
//    }
}
