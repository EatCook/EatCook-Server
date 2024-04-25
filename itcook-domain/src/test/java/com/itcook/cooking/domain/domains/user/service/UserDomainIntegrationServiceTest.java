package com.itcook.cooking.domain.domains.user.service;

import static com.itcook.cooking.domain.domains.post.enums.CookingType.BUNSIK;
import static com.itcook.cooking.domain.domains.post.enums.CookingType.CHINESE_FOOD;
import static com.itcook.cooking.domain.domains.post.enums.CookingType.KOREAN_FOOD;
import static com.itcook.cooking.domain.domains.post.enums.CookingType.SIDE_DISH;
import static com.itcook.cooking.domain.domains.post.enums.CookingType.WESTERN_FOOD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.IntegrationTestSupport;
import com.itcook.cooking.domain.domains.post.enums.CookingType;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.entity.UserCookingTheme;
import com.itcook.cooking.domain.domains.user.enums.LifeType;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserBadge;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.enums.UserState;
import com.itcook.cooking.domain.domains.user.repository.UserCookingThemeRepository;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageLeaveUser;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUpdateProfile;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUserDto;
import com.itcook.cooking.domain.domains.user.service.dto.UserUpdateInterestCook;
import com.itcook.cooking.domain.domains.user.service.dto.response.UserReadInterestCookResponse;
import com.itcook.cooking.infra.redis.event.UserLeaveEvent;
import com.itcook.cooking.infra.redis.event.UserLeaveEventListener;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class UserDomainIntegrationServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserDomainService userDomainService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCookingThemeRepository userCookingThemeRepository;

    @MockBean
    private CacheManager cacheManager;

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
    @DisplayName("추가 회원가입 요청 성공")
    void addSignup() {
        //given
        ItCookUser saveUser = createUser("user@test.com", "잇쿡1");
        List<CookingType> cookingTypes = List.of(KOREAN_FOOD, SIDE_DISH, WESTERN_FOOD);
        ItCookUser user = ItCookUser.builder()
            .id(saveUser.getId())
            .email(saveUser.getEmail())
            .password("cook12345")
            .providerType(ProviderType.COMMON)
            .nickName("뉴잇쿡")
            .userRole(UserRole.USER)
            .build();

        //when
        ItCookUser addSignupUser = userDomainService.addSignup(user, cookingTypes);

        //then
        List<UserCookingTheme> cookingThemes = userCookingThemeRepository.findAllByUserId(
            saveUser.getId());

        assertThat(addSignupUser.getNickName()).isEqualTo("뉴잇쿡");
        assertThat(addSignupUser.getLifeType()).isNull();
        assertThat(cookingThemes).hasSize(3)
            .extracting("cookingType")
            .containsExactlyInAnyOrder(
                KOREAN_FOOD,
                SIDE_DISH,
                WESTERN_FOOD
            )
            ;
    }
    @Test
    @DisplayName("넘어가기한 LifeType과 CookingTypes을 받은 ,추가 회원가입 요청 성공")
    void addSignupEmpty() {
        //given
        ItCookUser saveUser = createUser("user@test.com", "잇쿡1");
        List<CookingType> cookingTypes = List.of();
        ItCookUser user = ItCookUser.builder()
            .id(saveUser.getId())
            .email(saveUser.getEmail())
            .password("cook12345")
            .providerType(ProviderType.COMMON)
            .nickName("뉴잇쿡")
            .userRole(UserRole.USER)
            .build();

        //when
        ItCookUser addSignupUser = userDomainService.addSignup(user, cookingTypes);

        //then
        List<UserCookingTheme> cookingThemes = userCookingThemeRepository.findAllByUserId(
            saveUser.getId());

        assertThat(addSignupUser.getNickName()).isEqualTo("뉴잇쿡");
        assertThat(addSignupUser.getLifeType()).isNull();
        assertThat(cookingThemes).isEmpty();
    }

    @Test
    @DisplayName("유저 회원 탈퇴")
    void leaveUser() {
        //given
        ItCookUser user1 = createUser("user1@test.com", "잇쿡1");
        ItCookUser user2 = createUser("user2@test.com", "잇쿡2");
        MyPageLeaveUser leaveUser = MyPageLeaveUser.of(user1.getEmail());
        MyPageLeaveUser leaveUser2 = MyPageLeaveUser.of(user2.getEmail());

        doNothing().when(userLeaveEventListener).deleteToken(any(UserLeaveEvent.class));

        //when
        userDomainService.leaveUser(leaveUser);
        userDomainService.leaveUser(leaveUser2);

        //then
        Optional<ItCookUser> findUser = userRepository.findById(user1.getId());
        ItCookUser deleteUser = userRepository.findDeleteUserById(user1.getId()).get();

        assertThat(findUser.isEmpty()).isTrue();
        assertThat(deleteUser)
            .extracting("userState","email","profile","nickName")
            .containsExactly(UserState.DELETE, null, null, "탈퇴한 유저")
            ;
    }
    @Test
    @DisplayName("유저 회원 탈퇴시 유저가 존재하지 않은 예외 발생")
    void leaveUserNotFound() {
        //given
        MyPageLeaveUser leaveUser = MyPageLeaveUser.of("user@test.com");

        //when
        assertThatThrownBy(() -> userDomainService.leaveUser(leaveUser))
            .isInstanceOf(ApiException.class)
            .hasMessage("유저를 찾을 수 없습니다.")
            ;

    }

    @Test
    @DisplayName("이메일과, 관심 요리를 받아서,유저 생활타입과, cookingthemes 업데이트를 한다")
    void updateInterestCook() {
        //given
        //캐시 모의
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("interestCook")).thenReturn(cache);
        doNothing().when(cache).evict(any());

        ItCookUser user = createUser("user1@test.com", "잇쿡1");
        createCookingTheme(user.getId(), BUNSIK);
        createCookingTheme(user.getId(), CookingType.CHINESE_FOOD);

        UserUpdateInterestCook updateInterestCook = UserUpdateInterestCook.builder()
            .lifeType(LifeType.CONVENIENCE_STORE)
            .cookingTypes(
                List.of(BUNSIK, CookingType.DESERT, CookingType.LATE_NIGHT_SNACK))
            .build();

        //when
        userDomainService.updateInterestCook(user.getEmail(), updateInterestCook);

        //then
        ItCookUser findUser = userRepository.findById(user.getId()).get();
        List<UserCookingTheme> cookingThemes = userCookingThemeRepository.findAllByUserId(
            user.getId());

        verify(cache, times(1)).evict("user1@test.com");
        assertThat(findUser.getLifeType()).isEqualTo(updateInterestCook.lifeType());
        assertThat(cookingThemes).hasSize(3)
            .extracting("userId", "cookingType")
            .containsExactlyInAnyOrder(
                tuple(user.getId(), updateInterestCook.cookingTypes().get(0)),
                tuple(user.getId(), updateInterestCook.cookingTypes().get(1)),
                tuple(user.getId(), updateInterestCook.cookingTypes().get(2))
            )
            ;
    }
    @Test
    @DisplayName("빈 요청을 받아서 받아서,유저 생활타입과, cookingthemes 업데이트를 한다")
    void updateInterestCookEmtpy() {
        //given
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("interestCook")).thenReturn(cache);
        doNothing().when(cache).evict(any());

        ItCookUser user = createUser("user1@test.com", "잇쿡1");
        createCookingTheme(user.getId(), BUNSIK);
        createCookingTheme(user.getId(), CookingType.CHINESE_FOOD);

        UserUpdateInterestCook updateInterestCook = UserUpdateInterestCook.builder()
            .cookingTypes(
                List.of()
            )
            .build();

        //when
        userDomainService.updateInterestCook(user.getEmail(), updateInterestCook);

        //then
        ItCookUser findUser = userRepository.findById(user.getId()).get();
        List<UserCookingTheme> cookingThemes = userCookingThemeRepository.findAllByUserId(
            user.getId());

        verify(cache, times(1)).evict("user1@test.com");
        assertThat(findUser.getLifeType()).isNull();
        assertThat(cookingThemes).isEmpty();
    }

    @Test
    @DisplayName("관심요리 조회")
    void getInterestCook() {
        //given
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("interestCook")).thenReturn(cache);
        when(cache.get(any())).thenReturn(null);

        ItCookUser user = createUser("user1@test.com", "잇쿡1");
        createCookingTheme(user.getId(), BUNSIK);
        createCookingTheme(user.getId(), CookingType.CHINESE_FOOD);

        //when
        UserReadInterestCookResponse response = userDomainService.getInterestCook(
            user.getEmail());

        //then
        verify(cache, times(1)).get("user1@test.com");
        assertThat(response.lifeType()).isEqualTo("배달음식 단골고객");
        assertThat(response.cookingTypes()).hasSize(2)
            .contains("분식", "중식")
        ;
    }
    @Test
    @DisplayName("비어있는 관심요리 조회")
    void getInterestCookEmpty() {
        //given
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("interestCook")).thenReturn(cache);
        when(cache.get(any())).thenReturn(null);

        ItCookUser user = ItCookUser.builder()
            .email("user1@test.com")
            .password("cook12345")
            .providerType(ProviderType.COMMON)
            .nickName("잇쿡1")
            .userRole(UserRole.USER)
            .build();
        userRepository.save(user);

        //when
        UserReadInterestCookResponse response = userDomainService.getInterestCook(
            user.getEmail());
        System.out.println("response = " + response);

        //then
        verify(cache, times(1)).get("user1@test.com");
        assertThat(response.lifeType()).isNull();
        assertThat(response.cookingTypes()).isEmpty();
        ;
    }

    @Test
    @DisplayName("이메일과 비밀번호를 받아서, 회원가입을 시도한다.")
    void signup() {
        //given
        String email = "user@test.com";
        String password = "cook1234";

        //when
        ItCookUser user = userDomainService.signup(email, password);

        //then
        ItCookUser findUser = userRepository.findByEmail(email).get();
        assertThat(findUser)
            .extracting("email","password","providerType")
            .containsExactlyInAnyOrder(email, password, ProviderType.COMMON)
            ;
    }
    @Test
    @DisplayName("이메일 입력받지 않고, 회원가입 시도하여 예외가 발생한다.")
    void signupBlankEmail() {
        //given
        String email = null;
        String password = "cook1234";

        //when

        //then
        assertThatThrownBy(() -> userDomainService.signup(email, password))
            .isInstanceOf(IllegalArgumentException.class)
            ;
    }

    @Test
    @DisplayName("추가 회원가입 로직시, 닉네임이 중복되어 예외가 발생한다")
    void addSignupDuplicateNickName() {
        //given
        createUser("user@test.com", "잇쿡1");
        ItCookUser addSignupUser = ItCookUser.builder()
            .email("user1@test.com")
            .password("cook12345")
            .providerType(ProviderType.COMMON)
            .nickName("잇쿡1")
            .lifeType(LifeType.DELIVERY_FOOD)
            .userRole(UserRole.USER)
            .build();

        //when
        //then
        assertThatThrownBy(() -> userDomainService.addSignup(addSignupUser, List.of(BUNSIK, CHINESE_FOOD)))
            .isInstanceOf(ApiException.class)
            .hasMessage("이미 존재하는 닉네임입니다.")
            ;

    }

    private ItCookUser createUser(String username, String nickName) {
        ItCookUser user = ItCookUser.builder()
            .email(username)
            .password("cook12345")
            .providerType(ProviderType.COMMON)
            .nickName(nickName)
            .lifeType(LifeType.DELIVERY_FOOD)
            .userRole(UserRole.USER)
            .build();

        return userRepository.save(user);
    }

    public void createCookingTheme(Long userId, CookingType cookingType) {
        UserCookingTheme userCookingTheme = UserCookingTheme.createUserCookingTheme(userId,
            cookingType);
        userCookingThemeRepository.save(userCookingTheme);
    }

}