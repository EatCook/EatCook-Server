package com.itcook.cooking.domain.domains.user.enums;

import static org.assertj.core.api.Assertions.assertThat;

import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class LifeTypeTest {

    @ParameterizedTest
    @DisplayName("lifetype 문자열을 받아 LifeType 객체 변환")
    @CsvSource({"DIET,DIET", "HEALTH_DIET,HEALTH_DIET","CONVENIENCE_STORE,CONVENIENCE_STORE"})
    void getByName(String lifeTypeString, LifeType expected) {
        //given

        //when
        LifeType lifeType = LifeType.getByName(lifeTypeString);

        //then
        assertThat(lifeType).isEqualTo(expected)
        ;
    }
}