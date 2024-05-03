package com.itcook.cooking.domain.domains.user.service.dto.response;

import static com.itcook.cooking.domain.domains.post.enums.CookingType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import com.itcook.cooking.domain.domains.user.enums.LifeType;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserReadInterestCookResponseTest {


    @Test
    @DisplayName("UserReadInterestCookResponse로 변환 테스트(ENUM이 변환되는지)")
    void of() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");
        List<UserCookingTheme> cookingThemes = createCookingThemes(user,
            List.of(BUNSIK, CHINESE_FOOD));

        //when
        UserReadInterestCookResponse response = UserReadInterestCookResponse.of(
            user, cookingThemes);

        //then
        assertEquals(response.lifeType(),"배달음식 단골고객");
        assertEquals(response.cookingTypes().get(0),"분식");
        assertEquals(response.cookingTypes().get(1),"중식");
    }


    private ItCookUser createUser(String username, String nickName) {
        return ItCookUser.builder()
            .email(username)
            .password("cook12345")
            .providerType(ProviderType.COMMON)
            .nickName(nickName)
            .lifeType(LifeType.DELIVERY_FOOD)
            .userRole(UserRole.USER)
            .build();
    }

    public List<UserCookingTheme> createCookingThemes(ItCookUser user, List<CookingType> cookingTypes) {
        return user.createCookingThemes(cookingTypes);
    }


}