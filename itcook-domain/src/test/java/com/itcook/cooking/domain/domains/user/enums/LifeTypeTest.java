package com.itcook.cooking.domain.domains.user.enums;

import static org.assertj.core.api.Assertions.assertThat;

import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LifeTypeTest {

    @ParameterizedTest
    @DisplayName("lifetype 문자열을 받아 LifeType 객체 변환")
    @CsvSource({"다이어트만 n년째,DIET", "건강한 식단관리,HEALTH_DIET","편의점은 내 구역,CONVENIENCE_STORE"})
    void getByName(String lifeTypeString, LifeType expected) {
        //given

        //when
        LifeType lifeType = LifeType.getByName(lifeTypeString);

        //then
        assertThat(lifeType).isEqualTo(expected)
        ;
    }

    @ParameterizedTest
    @DisplayName("lifetype을 받아 LifeType 객체 변환")
    @CsvSource({"DIET,DIET", "HEALTH_DIET,HEALTH_DIET", "CONVENIENCE_STORE,CONVENIENCE_STORE"})
    void getByLifeType(String lifeTypeString, LifeType expected) {
        //given

        //when
        LifeType lifeType = LifeType.getByLifeType(lifeTypeString);

        //then
        assertThat(lifeType).isEqualTo(expected)
        ;
    }
}