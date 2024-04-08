package com.itcook.cooking.api.domains.user.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.itcook.cooking.api.IntegrationTestSupport;
import com.itcook.cooking.api.domains.user.service.dto.MyPagePostResponse;
import com.itcook.cooking.api.domains.user.service.dto.MyPageResponse;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.PostRepository;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserBadge;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import com.itcook.cooking.domain.domains.user.service.UserDomainService;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class MyPageUserCaseTest extends IntegrationTestSupport {

    @Autowired
    private MyPageUserCase myPageUserCase;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Test
    @DisplayName("마이 페이지를 조회한다.")
    void getMyPage() {
        //given
        String email = "user1@test.com";

        ItCookUser user1 = createUser(email, "잇쿡1");
        createPost(user1.getId(), "책제목1", "소개글1");
        createPost(user1.getId(), "책제목2", "소개글2");
        createPost(user1.getId(), "책제목3", "소개글3");

        //when
        MyPageResponse myPage = myPageUserCase.getMyPage(email);
        List<MyPagePostResponse> myPagePosts = myPage.getPosts();

        //then
        assertThat(myPage.getUserId()).isEqualTo(user1.getId());
        assertThat(myPage.getNickName()).isEqualTo(user1.getNickName());
        assertThat(myPage.getBadge()).isEqualTo(UserBadge.GIBBAB_GOSU.getDescription());
        assertThat(myPagePosts).hasSize(3)
            .extracting("title","introduction")
            .containsExactly(
                tuple("책제목3", "소개글3"),
                tuple("책제목2", "소개글2"),
                tuple("책제목1", "소개글1")
            )
        ;

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