package com.itcook.cooking.domain.domains.user.repository;

import com.itcook.cooking.ItCookUserCreate;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
class ItCookUserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("디비 테스트")
    void test1() {
        List<ItCookUser> itCookUsers = ItCookUserCreate.createUsers();
        for (ItCookUser itCookUser : itCookUsers) {
            System.out.println("user = " + itCookUser.getEmail());
        }
    }
}