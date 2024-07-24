package com.itcook.cooking.domain.domains.user.service;

import static com.itcook.cooking.domain.domains.post.domain.enums.CookingType.BUNSIK;
import static com.itcook.cooking.domain.domains.post.domain.enums.CookingType.CHINESE_FOOD;
import static com.itcook.cooking.domain.domains.post.domain.enums.CookingType.KOREAN_FOOD;
import static com.itcook.cooking.domain.domains.post.domain.enums.CookingType.SIDE_DISH;
import static com.itcook.cooking.domain.domains.post.domain.enums.CookingType.WESTERN_FOOD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.itcook.cooking.domain.common.errorcode.UserErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.IntegrationTestSupport;
import com.itcook.cooking.domain.domains.post.domain.enums.CookingType;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.entity.UserCookingTheme;
import com.itcook.cooking.domain.domains.user.domain.enums.LifeType;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.domain.enums.UserBadge;
import com.itcook.cooking.domain.domains.user.domain.enums.UserRole;
import com.itcook.cooking.domain.domains.user.domain.enums.UserState;
import com.itcook.cooking.domain.domains.user.domain.repository.UserCookingThemeRepository;
import com.itcook.cooking.domain.domains.user.domain.repository.UserRepository;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageUserInfoResponse;
import com.itcook.cooking.domain.domains.user.service.dto.UserUpdateInterestCook;
import com.itcook.cooking.domain.domains.user.service.dto.UserUpdatePassword;
import com.itcook.cooking.domain.domains.user.service.dto.response.UserReadInterestCookResponse;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class UserDomainIntegrationServiceTest extends IntegrationTestSupport {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserCookingThemeRepository userCookingThemeRepository;

    @MockBean
    private CacheManager cacheManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("마이페이지를 조회시, 유저 정보는 닉네임, 뱃지, 팔로워, 팔로잉을 반환한다.")
    void getMyPageInfo() {
        //given
        ItCookUser user1 = createUser("user1@test.com", "cook1234","잇쿡1");
        ItCookUser user2 = createUser("user2@test.com", "cook1234","잇쿡2");
        ItCookUser user3 = createUser("user3@test.com", "cook1234","잇쿡3");

        user1.addFollowing(user2.getId());
        user1.addFollowing(user3.getId());
        user2.addFollowing(user1.getId());
        user3.addFollowing(user2.getId());

        userRepository.saveAll(List.of(user1, user2, user3));
        //when
        MyPageUserInfoResponse myPageInfo = userService.getMyPageInfo(user1.getEmail());

        //then
        assertThat(myPageInfo.getUserId()).isEqualTo(user1.getId());
        assertThat(myPageInfo.getNickName()).isEqualTo("잇쿡1");
        assertThat(myPageInfo.getBadge()).isEqualTo(UserBadge.GIBBAB_FIRST.getDescription());
        assertThat(myPageInfo.getProviderType()).isEqualTo(ProviderType.COMMON);
        assertThat(myPageInfo.getFollowingCounts()).isEqualTo(2L);
        assertThat(myPageInfo.getFollowerCounts()).isEqualTo(1L);
    }


    @Test
    @DisplayName("추가 회원가입 요청 성공")
    void addSignup() {
        //given
        ItCookUser saveUser = createUser("user@test.com", "cook12345","잇쿡1");
        List<CookingType> cookingTypes = List.of(KOREAN_FOOD, SIDE_DISH, WESTERN_FOOD);
        ItCookUser user = ItCookUser.builder()
            .email(saveUser.getEmail())
            .providerType(ProviderType.COMMON)
            .nickName("뉴잇쿡")
            .userRole(UserRole.USER)
            .build();

        //when
        var response = userService.addSignup(user,
            null, cookingTypes);

        //then
        ItCookUser addSignupUser = userRepository.findByEmail("user@test.com").get();
        List<UserCookingTheme> userCookingThemes = userCookingThemeRepository.findAll();

        assertThat(userCookingThemes).hasSize(3)
                .extracting("cookingType")
                .containsExactlyInAnyOrder(KOREAN_FOOD, SIDE_DISH, WESTERN_FOOD);
        assertThat(addSignupUser.getNickName()).isEqualTo("뉴잇쿡");
        assertThat(addSignupUser.getLifeType()).isNull();
        assertThat(addSignupUser.getUserCookingThemes()).hasSize(3)
            .extracting("cookingType")
            .containsExactlyInAnyOrder(
                KOREAN_FOOD,
                SIDE_DISH,
                WESTERN_FOOD
            )
        ;
    }

    @Test
    @DisplayName("추가 회원가입 요청 성공")
    void addSignupDuplicateNick() {
        //given
        ItCookUser saveUser = createUser("user@test.com", "cook12345","잇쿡1");
        createUser("user2@test.com", "cook12345","잇쿡2");
        List<CookingType> cookingTypes = List.of(KOREAN_FOOD, SIDE_DISH, WESTERN_FOOD);
        ItCookUser user = ItCookUser.builder()
            .email(saveUser.getEmail())
            .providerType(ProviderType.COMMON)
            .nickName("잇쿡2")
            .userRole(UserRole.USER)
            .build();

        //when
        assertThatThrownBy(() -> userService.addSignup(user, null,
            cookingTypes))
            .isInstanceOf(ApiException.class)
            .hasMessage(UserErrorCode.ALREADY_EXISTS_NICKNAME.getDescription())
        ;

    }

    @Test
    @DisplayName("넘어가기한 LifeType과 CookingTypes을 받은 ,추가 회원가입 요청 성공")
    void addSignupEmpty() {
        //given
        ItCookUser saveUser = createUser("user@test.com", "cook12345","잇쿡1");
        List<CookingType> cookingTypes = List.of();
        ItCookUser user = ItCookUser.builder()
            .email(saveUser.getEmail())
            .providerType(ProviderType.COMMON)
            .nickName("뉴잇쿡")
            .userRole(UserRole.USER)
            .build();

        //when
        userService.addSignup(user,
            null, cookingTypes);

        //then
        ItCookUser addSignupUser = userRepository.findByEmail(saveUser.getEmail()).get();
        List<UserCookingTheme> userCookingThemes = userCookingThemeRepository.findAll();

        assertThat(userCookingThemes).isEmpty();
        assertThat(addSignupUser.getNickName()).isEqualTo("뉴잇쿡");
        assertThat(addSignupUser.getLifeType()).isNull();
        assertThat(addSignupUser.getUserCookingThemes()).isEmpty();
    }

    @Test
    @DisplayName("유저 회원 탈퇴")
    void leaveUser() {
        //given
        ItCookUser user1 = createUser("user1@test.com", "cook12345","잇쿡1");
        ItCookUser user2 = createUser("user2@test.com", "cook12345","잇쿡2");

        //when
        userService.leaveUser(user1.getEmail());
        userService.leaveUser(user2.getEmail());

        //then
        ItCookUser deleteUser = userRepository.findById(user1.getId()).get();

        assertThat(deleteUser)
            .extracting("userState", "email", "profile", "nickName")
            .containsExactly(UserState.DELETE, null, null, "탈퇴한 유저")
        ;
    }

    @Test
    @DisplayName("이메일과, 관심 요리를 받아서,유저 생활타입과, cookingthemes 업데이트를 한다")
    void updateInterestCook() {
        //given
        //캐시 모의
        ItCookUser user = createUser("user1@test.com", "잇쿡1",List.of(BUNSIK, CHINESE_FOOD));
//        createCookingThemes(user, List.of(BUNSIK, CHINESE_FOOD));

        UserUpdateInterestCook updateInterestCook = UserUpdateInterestCook.builder()
            .lifeType(LifeType.CONVENIENCE_STORE)
            .cookingTypes(
                List.of(BUNSIK, CookingType.DESERT, CookingType.LATE_NIGHT_SNACK))
            .build();

        //when
        userService.updateInterestCook(user.getEmail(), updateInterestCook.cookingTypes(),
            updateInterestCook.lifeType());

        //then
        ItCookUser findUser = userRepository.findById(user.getId()).get();

        assertThat(findUser.getLifeType()).isEqualTo(updateInterestCook.lifeType());
        assertThat(findUser.getUserCookingThemes()).hasSize(3)
            .extracting("user", "cookingType")
            .containsExactlyInAnyOrder(
                tuple(findUser, updateInterestCook.cookingTypes().get(0)),
                tuple(findUser, updateInterestCook.cookingTypes().get(1)),
                tuple(findUser, updateInterestCook.cookingTypes().get(2))
            )
        ;
    }

    @Test
    @DisplayName("빈 요청을 받아서 받아서,유저 생활타입과, cookingthemes 업데이트를 한다")
    void updateInterestCookEmtpy() {
        //given
        ItCookUser user = createUser("user1@test.com", "잇쿡1",List.of(BUNSIK, CHINESE_FOOD));
//        user.addUserCookingThemes(List.of(BUNSIK, CHINESE_FOOD));

        UserUpdateInterestCook updateInterestCook = UserUpdateInterestCook.builder()
            .cookingTypes(
                List.of()
            )
            .build();

        //when
        userService.updateInterestCook(user.getEmail(), updateInterestCook.cookingTypes(),
            updateInterestCook.lifeType());

        //then
        ItCookUser findUser = userRepository.findByEmail("user1@test.com").get();

        assertThat(findUser.getUserCookingThemes()).isEmpty();
    }

    @Test
    @DisplayName("관심요리 조회")
    void getInterestCook() {
        //given
        ItCookUser user = createUser("user1@test.com", "잇쿡1",List.of(BUNSIK, CHINESE_FOOD));
//        user.addUserCookingThemes(List.of(BUNSIK, CHINESE_FOOD));

        //when
        UserReadInterestCookResponse response = userService.getInterestCook(
            user.getEmail());

        //then
//        verify(cache, times(1)).get("user1@test.com");
        assertThat(response.lifeType()).isEqualTo("배달음식 단골고객");
        assertThat(response.cookingTypes()).hasSize(2)
            .contains("분식", "중식")
        ;
    }

    @Test
    @DisplayName("비어있는 관심요리 조회")
    void getInterestCookEmpty() {
        //given
        String email = "user1@test.com";
        createUser(email, "잇쿡1",List.of());

        //when
        UserReadInterestCookResponse response = userService.getInterestCook(
            email);

        //then
//        verify(cache, times(1)).get("user1@test.com");
        assertThat(response.lifeType()).isEqualTo(LifeType.DELIVERY_FOOD.getLifeTypeName());
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
        ItCookUser user = userService.signup(email, password);

        //then
        ItCookUser findUser = userRepository.findByEmail(email).get();
        assertThat(findUser)
            .extracting("email", "password", "providerType")
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
        assertThatThrownBy(() -> userService.signup(email, password))
            .isInstanceOf(IllegalArgumentException.class)
        ;
    }
    @Test
    @DisplayName("중복 이메일로 회원가입 시도시 예외가 발생한다.")
    void signupDuplicateEmail() {
        //given
        createUser("user@test.com","cook12345","잇쿡");

        //when //then
        assertThatThrownBy(() -> userService.signup("user@test.com", "cook1234"))
            .isInstanceOf(ApiException.class)
            .hasMessage(UserErrorCode.ALREADY_EXISTS_USER.getDescription())
        ;
    }

    @Test
    @DisplayName("추가 회원가입 로직시, 닉네임이 중복되어 예외가 발생한다")
    void addSignupDuplicateNickName() {
        //given
        createUser("user@test.com", "cook12345","잇쿡1");
        ItCookUser addSignupUser = ItCookUser.builder()
            .email("user@test.com")
            .password("cook12345")
            .providerType(ProviderType.COMMON)
            .nickName("잇쿡1")
            .lifeType(LifeType.DELIVERY_FOOD)
            .userRole(UserRole.USER)
            .build();

        //when
        //then
        assertThatThrownBy(
            () -> userService.addSignup(addSignupUser, null,
                List.of(BUNSIK, CHINESE_FOOD)))
            .isInstanceOf(ApiException.class)
            .hasMessage("이미 존재하는 닉네임입니다.")
        ;

    }

    @Test
    @DisplayName("프로필(닉네임) 업데이트 시도한다")
    void updateProfile() {
        //given
        ItCookUser user = createUser("user@test.com", "cook12345","잇쿡");
        String newNickName = "잇쿡2";

        //when
        userService.updateProfile(user.getEmail(), newNickName);

        //then
        ItCookUser findUser = userRepository.findByEmail(user.getEmail()).get();
        assertThat(findUser.getNickName()).isEqualTo("잇쿡2")
        ;
    }

    @Test
    @DisplayName("프로필(닉네임) 업데이트 시도시 중복닉으로 예외 발생")
    void updateProfileDuplicateNickName() {
        //given
        ItCookUser user = createUser("user@test.com", "cook12345","잇쿡2");
        String newNickName = "잇쿡2";

        //when
        //then
        assertThatThrownBy(() -> userService.updateProfile(user.getEmail(), newNickName))
            .isInstanceOf(ApiException.class)
            .hasMessage(UserErrorCode.ALREADY_EXISTS_NICKNAME.getDescription())
        ;
    }

    @Test
    @DisplayName("현재비밀번호와 새로운 비밀번호를 받아 비밀번호 변경을 시도한다.")
    void changePassword() {
        //given
        ItCookUser user = createUser("user@test.com", "cook1234", "잇쿡2");

        UserUpdatePassword updatePassword = UserUpdatePassword.builder()
            .email(user.getEmail())
            .rawCurrentPassword("cook1234")
            .newPassword("test1234")
            .build();

        //when
        userService.changePassword(updatePassword);

        //then
        ItCookUser findUser = userRepository.findByEmail(user.getEmail()).get();

        assertThat(passwordEncoder.matches("test1234", findUser.getPassword()))
            .isTrue();

    }
    @Test
    @DisplayName("현재비밀번호와 새로운 비밀번호를 받아 비밀번호 변경을 시도하지만, 현재 비밀번호가 맞지 않아 예외 발생한다.")
    void changePasswordNotEqualCurrentPassword() {
        //given
        ItCookUser user = createUser("user@test.com", "cook1234", "잇쿡2");

        UserUpdatePassword updatePassword = UserUpdatePassword.builder()
            .email(user.getEmail())
            .rawCurrentPassword("cook124")
            .newPassword("test1234")
            .build();

        //when

        //then
        assertThatThrownBy(() -> userService.changePassword(updatePassword))
            .isInstanceOf(ApiException.class)
            .hasMessage(UserErrorCode.NOT_EQUAL_PASSWORD.getDescription())
            ;


    }

    @Test
    @DisplayName("비밀번호 재발급을 시도한다.")
    void issueTemporaryPassword() {
        //given
        ItCookUser user = createUser("user@test.com", "cook1234", "잇쿡2");

        //when
        String temporaryPassword = userService.issueTemporaryPassword(user.getEmail());

        //then
        ItCookUser findUser = userRepository.findByEmail(user.getEmail()).get();

        assertThat(passwordEncoder.matches(temporaryPassword, findUser.getPassword()))
            .isTrue();
    }

    @Test
    @DisplayName("계정 찾기 후 새비밀번호로 변경합니다.")
    void findUserNewPassword() {
        //given
        ItCookUser user = createUser("user@test.com", "cook1234", "잇쿡2");
        ItCookUser entity = ItCookUser.builder()
            .email("user@test.com")
            .password("cook12345")
            .build();

        //when
        userService.changePassword(entity);

        //then
        ItCookUser findUser = userRepository.findByEmail(user.getEmail()).get();
        assertThat(passwordEncoder.matches("cook12345", findUser.getPassword()))
            .isTrue();

    }

    private ItCookUser createUser(String username, String password, String nickName) {
        ItCookUser user = ItCookUser.builder()
            .email(username)
            .password(passwordEncoder.encode(password))
            .providerType(ProviderType.COMMON)
            .nickName(nickName)
            .lifeType(LifeType.DELIVERY_FOOD)
            .userRole(UserRole.USER)
            .build();

        return userRepository.save(user);
    }

    private ItCookUser createUser(String username, String nickName, List<CookingType> cookingTypes) {
        ItCookUser user = ItCookUser.builder()
            .email(username)
            .password("cook12345")
            .providerType(ProviderType.COMMON)
            .nickName(nickName)
            .lifeType(LifeType.DELIVERY_FOOD)
            .userRole(UserRole.USER)
            .build();
        user.addUserCookingThemes(cookingTypes);

        return userRepository.save(user);
    }

    private ItCookUser createUser(String username, String nickName, LifeType lifeType,
        List<CookingType> cookingTypes) {
        ItCookUser user = ItCookUser.builder()
            .email(username)
            .password("cook12345")
            .providerType(ProviderType.COMMON)
            .nickName(nickName)
            .lifeType(lifeType)
            .userRole(UserRole.USER)
            .build();
        user.addUserCookingThemes(cookingTypes);

        return userRepository.save(user);
    }


    public List<UserCookingTheme> createCookingThemes(ItCookUser user, List<CookingType> cookingTypes) {
        List<UserCookingTheme> userCookingThemes = new ArrayList<>();
        for (CookingType cookingType : cookingTypes) {
            UserCookingTheme userCookingTheme = UserCookingTheme.builder()
                .user(user)
                .cookingType(cookingType)
                .build();
            userCookingThemes.add(userCookingTheme);
        }
        return userCookingThemeRepository.saveAll(userCookingThemes);
    }

}