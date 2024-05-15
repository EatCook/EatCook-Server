package com.itcook.cooking.domain.domains.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.itcook.cooking.domain.domains.IntegrationTestSupport;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.PostRepository;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserBadge;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.persistence.EntityManager;
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

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("특정 아이디에 해당하는 팔로워 수를 구한다")
    void getFollower() {
        //given
        ItCookUser user1 = createUser("user1@test.com", "잇쿡1");
        ItCookUser user2 = createUser("user2@test.com", "잇쿡2");
        ItCookUser user3 = createUser("user3@test.com", "잇쿡3");

        // user1 팔로잉 1, 팔로워 2
        user1.addFollowing(user2.getId());
        user1.addFollowing(user3.getId());

        // user2 팔로잉 1, 팔로워 2
        user2.addFollowing(user1.getId());

        // user3  팔로잉 2, 팔로워 0
        user3.addFollowing(user1.getId());
        user3.addFollowing(user2.getId());


        //when
        long user1Followers = userQueryRepository.getFollowerCounts(user1.getId());
        long user2Followers = userQueryRepository.getFollowerCounts(user2.getId());
        long user3Followers = userQueryRepository.getFollowerCounts(user3.getId());

        //then
        assertThat(user1Followers).isEqualTo(2);
        assertThat(user2Followers).isEqualTo(2);
        assertThat(user3Followers).isEqualTo(1);
    }


    @Test
    @DisplayName("게시글수 특정 개수 이상 작성하여 유저 뱃지 업데이트")
    void updateUserBadge() {
        //given
        ItCookUser user = createUser("user1@test.com", "잇쿡1");
        ItCookUser user2 = createUser("user2@test.com", "잇쿡2");
        List<Post> list = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            Post post = createPost(user.getId(), "요리" + i, "상세" + i);
            list.add(post);
        }
        postRepository.saveAll(list);


        //when
        userQueryRepository.updateUserBadge();

        em.flush();
        em.clear();

        //then
        List<ItCookUser> users = userRepository.findAll();
        assertThat(users.get(0).getBadge()).isEqualTo(UserBadge.GIBBAB_GOSU);
        assertThat(users.get(1).getBadge()).isEqualTo(UserBadge.GIBBAB_NORMAL);
    }

    @Test
    @DisplayName("업데이트 할 유저가 없지만 업데이트를 진행한다.")
    void updateUserEmpty() {
        //given
        ItCookUser user = createUser("user1@test.com", "잇쿡1");
        ItCookUser user2 = createUser("user2@test.com", "잇쿡2");

        //when
        userQueryRepository.updateUserBadge();

        //then
        List<ItCookUser> users = userRepository.findAll();
        assertThat(users.get(0).getBadge()).isEqualTo(UserBadge.GIBBAB_NORMAL);
        assertThat(users.get(1).getBadge()).isEqualTo(UserBadge.GIBBAB_NORMAL);
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

    private Post createPost(Long userId, String title, String introduction) {
        Post post = Post.builder()
            .recipeName(title)
            .introduction(introduction)
            .postImagePath("image1")
            .userId(userId)
            .postFlag(PostFlag.ACTIVATE)
            .build();
        return post;
    }

    private void createPost(int end, Long userId) {
        List<Post> posts = new ArrayList<>();
        Post post = Post.builder()
            .recipeName("요리")
            .introduction("상세")
            .postImagePath("image1")
            .userId(userId)
            .postFlag(PostFlag.ACTIVATE)
            .build();
        IntStream.rangeClosed(1, end)
            .forEach(i -> posts.add(post));

        postRepository.saveAll(posts);
    }


}