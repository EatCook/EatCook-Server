package com.itcook.cooking.domain.domains.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.itcook.cooking.domain.domains.IntegrationTestSupport;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class UserQueryRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserQueryRepository userQueryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("특정 아이디에 해당하는 팔로워 수를 구한다")
    void getFollower() {
        //given
        ItCookUser user1 = createUser("user1@test.com", "잇쿡1");
        ItCookUser user2 = createUser("user2@test.com", "잇쿡2");
        ItCookUser user3 = createUser("user3@test.com", "잇쿡3");

        // user1 팔로잉 1, 팔로워 2
        user1.addFollowing(2L);
        user1.addFollowing(3L);

        // user2 팔로잉 1, 팔로워 2
        user2.addFollowing(1L);

        // user3  팔로잉 2, 팔로워 0
        user3.addFollowing(1L);
        user3.addFollowing(2L);


        //when
        long user1Followers = userQueryRepository.getFollowerCounts(user1.getId());
        long user2Followers = userQueryRepository.getFollowerCounts(user2.getId());
        long user3Followers = userQueryRepository.getFollowerCounts(user3.getId());

        //then
        assertThat(user1Followers).isEqualTo(2);
        assertThat(user2Followers).isEqualTo(2);
        assertThat(user3Followers).isEqualTo(1);
    }

    private ItCookUser createUser(String username, String nickName) {
        ItCookUser user = ItCookUser.builder()
            .email(username)
            .password("cook12345")
            .providerType(ProviderType.COMMON)
            .nickName(nickName)
            .userRole(UserRole.USER)
            .build();

        return userRepository.save(user);
    }

}