package com.itcook.cooking.domain.domains.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.IntegrationTestSupport;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserBadge;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageLeaveUser;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUpdateProfile;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUserDto;
import com.itcook.cooking.infra.redis.event.UserLeaveEvent;
import com.itcook.cooking.infra.redis.event.UserLeaveEventListener;
import java.util.List;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class UserDomainIntegrationServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserDomainService userDomainService;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserLeaveEventListener userLeaveEventListener;

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

        userRepository.saveAll(List.of(user1, user2, user3));
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

    @Test
    @DisplayName("이메일, 닉네임을 받아서 프로필을 업데이트 한다")
    void updateProfile() {
        //given
        ItCookUser user1 = createUser("user1@test.com", "잇쿡1");
//        ItCookUser user2 = createUser("user2@test.com", "잇쿡2");

        MyPageUpdateProfile updateProfile = MyPageUpdateProfile.builder()
            .email(user1.getEmail())
            .nickName("잇쿡2")
            .build();
        //when
        userDomainService.updateProfile(updateProfile);

        //then
        ItCookUser findUser = userRepository.findById(user1.getId()).get();

        assertThat(findUser.getNickName()).isEqualTo("잇쿡2");
    }
    @Test
    @DisplayName("프로필 업데이트시 닉네임 중복으로 예외가 발생한다.")
    void updateProfileDuplicateNick() {
        //given
        ItCookUser user1 = createUser("user1@test.com", "잇쿡1");
        ItCookUser user2 = createUser("user2@test.com", "잇쿡2");

        MyPageUpdateProfile updateProfile = MyPageUpdateProfile.builder()
            .email(user1.getEmail())
            .nickName("잇쿡2")
            .build();
        //when


        //then
        assertThatThrownBy(() -> userDomainService.updateProfile(updateProfile))
            .isInstanceOf(ApiException.class)
            .hasMessage("이미 존재하는 닉네임입니다.")
            ;

    }

    @Test
    @DisplayName("유저 회원 탈퇴")
    void leaveUser() {
        //given
        ItCookUser user1 = createUser("user1@test.com", "잇쿡1");
        MyPageLeaveUser leaveUser = MyPageLeaveUser.builder()
            .email(user1.getEmail())
            .build();

        doNothing().when(userLeaveEventListener).deleteToken(any(UserLeaveEvent.class));

        //when
        userDomainService.leaveUser(leaveUser);

        //then
        Optional<ItCookUser> findUser = userRepository.findById(user1.getId());

        assertThat(findUser.isEmpty()).isTrue();
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