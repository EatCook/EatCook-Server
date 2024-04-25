package com.itcook.cooking.api.domains.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.itcook.cooking.api.IntegrationTestSupport;
import com.itcook.cooking.api.domains.user.service.dto.MyPagePasswordServiceDto;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.PostRepository;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserBadge;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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

    @Test
    @DisplayName("마이 페이지를 조회한다.")
    void getMyPage() throws JsonProcessingException {
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
        assertThat(myPage.getBadge()).isEqualTo(UserBadge.GIBBAB_GOSU.getDescription());
        assertThat(myPage.getPosts().content()).hasSize(3)
            .extracting("recipeName","introduction")
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
            .currentPassword(currentPassword)
            .newPassword(newPassword)
            .build();
            ;

        //when
        myPageUseCase.changePassword(passwordServiceDto);

        //then
        ItCookUser findUser = userRepository.findById(user.getId()).get();

        assertThat(passwordEncoder.matches(newPassword,findUser.getPassword()))
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
            .currentPassword(currentPassword)
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
    @DisplayName("비밀번호 변경 요청시, 새로운 비밀번호가 비즈니스 비밀번호 규칙에 맞지 않아 예외 발생")
    void changePasswordNotMatches() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");

        String currentPassword = "cook12345";
        String newPassword = "cook12";

        MyPagePasswordServiceDto passwordServiceDto = MyPagePasswordServiceDto.builder()
            .email(user.getEmail())
            .currentPassword(currentPassword)
            .newPassword(newPassword)
            .build();
            ;

        //when //then
        assertThatThrownBy(() -> myPageUseCase.changePassword(passwordServiceDto))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("패스워드는 8자리 이상이어야 하며, 영문과 숫자를 포함해야 합니다.")
            ;
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

    private void createPost(Long userId, String title, String introduction) {
        Post post = Post.builder()
            .recipeName(title)
            .introduction(introduction)
            .postImagePath("image1")
            .userId(userId)
            .postFlag(PostFlag.ACTIVATE)
            .build();
        postRepository.save(post);
    }


}