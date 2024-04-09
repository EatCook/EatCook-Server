package com.itcook.cooking.domain.domains.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.itcook.cooking.domain.domains.IntegrationTestSupport;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserBadge;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUserDto;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class UserDomainIntegrationServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserDomainService userDomainService;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("마이페이지를 조회시, 유저 정보는 닉네임, 뱃지, 팔로워, 팔로잉을 반환한다.")
    void getMyPageInfo() {
        //given
        ItCookUser user1 = createUser("user1@test.com", "잇쿡1");
        ItCookUser user2 = createUser("user2@test.com", "잇쿡2");
        ItCookUser user3 = createUser("user3@test.com", "잇쿡3");

        user1.addFollowing(user2.getId());
        user1.addFollowing(user3.getId());
        user2.addFollowing(user1.getId());
        user3.addFollowing(user2.getId());

        userRepository.saveAll(List.of(user1,user2,user3));
        //when
        MyPageUserDto myPageInfo = userDomainService.getMyPageInfo(user1.getEmail());

        //then
        assertThat(myPageInfo.getUserId()).isEqualTo(user1.getId());
        assertThat(myPageInfo.getNickName()).isEqualTo("잇쿡1");
        assertThat(myPageInfo.getBadge()).isEqualTo(UserBadge.GIBBAB_GOSU.getDescription());
        assertThat(myPageInfo.getProviderType()).isEqualTo(ProviderType.COMMON);
        assertThat(myPageInfo.getFollowingCounts()).isEqualTo(2L);
        assertThat(myPageInfo.getFollowerCounts()).isEqualTo(1L);
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