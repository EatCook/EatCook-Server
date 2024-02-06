package com.itcook.cooking.domain.domains.user.repository;

import com.itcook.cooking.ItCookUserCreate;
import com.itcook.cooking.domain.domains.user.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("디비 테스트")
    void test1() {
        List<User> users = ItCookUserCreate.createUsers();
        for (User user : users) {
            System.out.println("user = " + user.getEmail());
        }
    }
}