package com.itcook.cooking.api.domains.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.itcook.cooking.api.IntegrationTestSupport;
import com.itcook.cooking.api.domains.user.service.dto.MyPagePasswordServiceDto;
import com.itcook.cooking.api.domains.user.service.dto.response.MyPageArchivePostsResponse;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.archive.entity.Archive;
import com.itcook.cooking.domain.domains.archive.repository.ArchiveRepository;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.PostRepository;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.EventAlertType;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.ServiceAlertType;
import com.itcook.cooking.domain.domains.user.enums.UserBadge;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.enums.UserState;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageAlertUpdate;
import com.itcook.cooking.domain.domains.user.service.dto.MyPageUpdateProfile;
import com.itcook.cooking.domain.domains.user.service.dto.response.MyPageSetUpResponse;
import com.itcook.cooking.domain.common.events.user.UserLeaveEvent;
import java.util.List;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.transaction.annotation.Transactional;

@RecordApplicationEvents
@Transactional
class MyPageUseCaseTest extends IntegrationTestSupport {

    @Autowired
    private MyPageUseCase myPageUseCase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ApplicationEvents applicationEvents;

    @Autowired
    private ArchiveRepository archiveRepository;

    @MockBean
    private CacheManager cacheManager;

    @Test
    @DisplayName("마이 페이지를 조회한다.")
    void getMyPage()  {
        //given
        String email = "user1@test.com";

        ItCookUser user1 = createUser(email, "잇쿡1");
        createPost(user1.getId(), "책제목1", "소개글1");
        createPost(user1.getId(), "책제목2", "소개글2");
        createPost(user1.getId(), "책제목3", "소개글3");

        //when
        var myPage = myPageUseCase.getMyPage(email,
            PageRequest.of(0, 10));

        //then

        assertThat(myPage.getUserId()).isEqualTo(user1.getId());
        assertThat(myPage.getNickName()).isEqualTo(user1.getNickName());
        assertThat(myPage.getBadge()).isEqualTo(UserBadge.GIBBAB_NORMAL.getDescription());
        assertThat(myPage.getPosts().content()).hasSize(3)
            .extracting("recipeName", "introduction")
            .containsExactly(
                tuple("책제목3", "소개글3"),
                tuple("책제목2", "소개글2"),
                tuple("책제목1", "소개글1")
            )
        ;

    }

    @Test
    @DisplayName("비밀번호 변경을 한다.")
    void changePassword() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");

        String currentPassword = "cook12345";
        String newPassword = "cook1234";

        MyPagePasswordServiceDto passwordServiceDto = MyPagePasswordServiceDto.builder()
            .email(user.getEmail())
            .rawCurrentPassword(currentPassword)
            .newPassword(newPassword)
            .build();
        ;

        //when
        myPageUseCase.changePassword(passwordServiceDto);

        //then
        ItCookUser findUser = userRepository.findById(user.getId()).get();

        assertThat(passwordEncoder.matches(newPassword, findUser.getPassword()))
            .isTrue()
        ;
    }

    @Test
    @DisplayName("비밀번호 변경 요청시, 현재 비밀번호가 맞지 않아 예외 발생")
    void changePasswordValidateCurrentPassword() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");

        String currentPassword = "cook1234";
        String newPassword = "cook1234";

        MyPagePasswordServiceDto passwordServiceDto = MyPagePasswordServiceDto.builder()
            .email(user.getEmail())
            .rawCurrentPassword(currentPassword)
            .newPassword(newPassword)
            .build();
        ;

        //when //then
        assertThatThrownBy(() -> myPageUseCase.changePassword(passwordServiceDto))
            .isInstanceOf(ApiException.class)
            .hasMessage("현재 비밀번호와 일치하지 않습니다.")
        ;
    }

    @Test
    @Disabled
    @DisplayName("비밀번호 변경 요청시, 새로운 비밀번호가 비즈니스 비밀번호 규칙에 맞지 않아 예외 발생")
    void changePasswordNotMatches() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");

        String currentPassword = "cook12345";
        String newPassword = "cook12";

        MyPagePasswordServiceDto passwordServiceDto = MyPagePasswordServiceDto.builder()
            .email(user.getEmail())
            .rawCurrentPassword(currentPassword)
            .newPassword(newPassword)
            .build();
        ;

        //when //then
        assertThatThrownBy(() -> myPageUseCase.changePassword(passwordServiceDto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("패스워드는 8자리 이상이어야 하며, 영문과 숫자를 포함해야 합니다.")
        ;
    }

    @Test
    @DisplayName("회원 탈퇴를 시도한다")
    void leaveUser() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡");

        //when
        myPageUseCase.leaveUser(user.getEmail());

        //then
        long count = applicationEvents.stream(UserLeaveEvent.class).count();
        ItCookUser findUser = userRepository.findByEmail(user.getEmail()).get();

        assertThat(count).isEqualTo(1);
        assertThat(findUser)
            .extracting("userState", "email", "profile", "nickName", "serviceAlertType",
                "eventAlertType")
            .containsExactlyInAnyOrder(UserState.DELETE, null, null, "탈퇴한 유저",
                ServiceAlertType.DISABLED, EventAlertType.DISABLED)
        ;
    }

    @Test
    @DisplayName("회원 탈퇴를 시도하지만, 유저가 존재하지 않아 예외 발")
    void leaveUserNotFoundUser() {
        //given
        //when

        //then
        assertThatThrownBy(() -> myPageUseCase.leaveUser("user@test.com"))
            .isInstanceOf(ApiException.class)
            .hasMessage("유저를 찾을 수 없습니다.")
        ;
    }

    @Test
    @DisplayName("닉네임을 받아서 프로필 업데이트를 시도한다.")
    void updateProfile() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡");
        MyPageUpdateProfile updateProfile = MyPageUpdateProfile.builder()
            .nickName("잇쿡2")
            .email(user.getEmail())
            .build();

        //when
        myPageUseCase.updateProfile(updateProfile);

        //then
        ItCookUser findUser = userRepository.findByEmail(user.getEmail()).get();
        assertThat(findUser.getNickName()).isEqualTo("잇쿡2");
    }

    @Test
    @DisplayName("마이 프로필 조회 요청")
    void getMyPageSetUp() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡");
        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("mypage")).thenReturn(cache);
        when(cache.get(any())).thenReturn(null);

        //when
        MyPageSetUpResponse response = myPageUseCase.getMyPageSetUp(user.getEmail());

        //then
        verify(cache, times(1)).get("user:#email");
        assertThat(response)
            .extracting("serviceAlertType","eventAlertType")
            .containsExactly(ServiceAlertType.DISABLED, EventAlertType.DISABLED)
            ;

    }

    @Test
    @DisplayName("마이 프로필 설정 변경을 시도한다.")
    void updateMyPageSetUp() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡");
        MyPageAlertUpdate myPageAlertUpdate = MyPageAlertUpdate.builder()
            .serviceAlertType(ServiceAlertType.ACTIVATE)
            .eventAlertType(EventAlertType.ACTIVATE)
            .build();

        Cache cache = mock(Cache.class);
        when(cacheManager.getCache("mypage")).thenReturn(cache);
        doNothing().when(cache).evict(any());

        //when
        myPageUseCase.updateMyPageSetUp(user.getEmail(), myPageAlertUpdate);

        //then
        ItCookUser findUser = userRepository.findByEmail(user.getEmail()).get();

        verify(cache, times(1)).evict("user:#email");
        assertThat(findUser)
            .extracting("serviceAlertType","eventAlertType")
            .containsExactly(ServiceAlertType.ACTIVATE, EventAlertType.ACTIVATE)
            ;
    }

    @Test
    @DisplayName("내가 저장한 북마크 보관함 조회")
    void getArchivePosts() {
        //given
        ItCookUser user1 = createUser("user1@test.com", "잇쿡1");
        ItCookUser user2 = createUser("user2@test.com", "잇쿡2");

        Post post1 = createPost(user2.getId(), "요리1", "상세1");
        Post post2 = createPost(user2.getId(), "요리2", "상세2");
        Post post3 = createPost(user2.getId(), "요리3", "상세3");

        createArchive(post1.getId(), user1.getId());
        createArchive(post2.getId(), user1.getId());
        createArchive(post3.getId(), user1.getId());

        //when
        List<MyPageArchivePostsResponse> response = myPageUseCase.getArchivePosts(
            user1.getEmail());

        //then
        assertThat(response)
            .extracting("postId","postImagePath")
            .containsExactly(
                tuple(post3.getId(), post3.getPostImagePath()),
                tuple(post2.getId(), post2.getPostImagePath()),
                tuple(post1.getId(), post1.getPostImagePath())
            );
    }

    private void createArchive(Long postId, Long userId) {
        Archive archive = Archive.builder()
            .postId(postId)
            .itCookUserId(userId)
            .build();

        
        archiveRepository.save(archive);
    }

    private ItCookUser createUser(String username, String nickName) {
        ItCookUser user = ItCookUser.builder()
            .email(username)
            .password(passwordEncoder.encode("cook12345"))
            .providerType(ProviderType.COMMON)
            .nickName(nickName)
            .userRole(UserRole.USER)
            .build();

        return userRepository.save(user);
    }

    private Post createPost(Long userId, String title, String introduction) {
        Post post = Post.builder()
            .recipeName(title)
            .introduction(introduction)
            .postImagePath("image1")
            .userId(userId)
            .postFlag(PostFlag.ACTIVATE)
            .build();
        return postRepository.save(post);
    }


}