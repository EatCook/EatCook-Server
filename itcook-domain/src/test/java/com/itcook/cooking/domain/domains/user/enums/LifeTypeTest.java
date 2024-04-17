package com.itcook.cooking.domain.domains.user.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LifeTypeTest {

    @ParameterizedTest
    @DisplayName("lifetype 문자열을 받아 LifeType 객체 변환")
    @CsvSource({"다이어트만 n번째,DIET", "건강한 식단관리,HEALTH_DIET","편의점은 내 구역,CONVENIENCE_STORE"})
    void getByName(String lifeTypeString, LifeType expected) {
        //given

        //when
        LifeType lifeType = LifeType.getByName(lifeTypeString);

        //then
        assertThat(lifeType).isEqualTo(expected)
        ;
    }
}