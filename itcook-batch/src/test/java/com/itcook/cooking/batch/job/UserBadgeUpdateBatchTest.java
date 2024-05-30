package com.itcook.cooking.batch.job;

import static com.itcook.cooking.domain.domains.user.enums.UserBadge.GIBBAB_FINAL;
import static com.itcook.cooking.domain.domains.user.enums.UserBadge.GIBBAB_FIRST;
import static com.itcook.cooking.domain.domains.user.enums.UserBadge.GIBBAB_FOURTH;
import static com.itcook.cooking.domain.domains.user.enums.UserBadge.GIBBAB_SECOND;
import static com.itcook.cooking.domain.domains.user.enums.UserBadge.GIBBAB_THIRD;
import static org.assertj.core.api.Assertions.assertThat;

import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.PostRepository;
import com.itcook.cooking.domain.domains.user.adaptor.UserAdaptor;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserBadge;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ComponentScan(basePackages = "com.itcook.cooking")
@ActiveProfiles("test")
@EnableAutoConfiguration
@EnableBatchProcessing
@SpringBatchTest
@SpringBootTest
@TestPropertySource(properties = "chunkSize=1")
class UserBadgeUpdateBatchTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @AfterEach
    void tearDown() {
        userRepository.deleteAllInBatch();
        postRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("유저 뱃지 업데이트 배치잡을 시도한다.")
    void userBadgeUpdateJob() throws Exception {
        //given
        ItCookUser user1 = createUser("user1@test.com", "잇쿡1");
        ItCookUser user2 = createUser("user2@test.com", "잇쿡2");
        ItCookUser user3 = createUser("user3@test.com", "잇쿡3");
        ItCookUser user4 = createUser("user4@test.com", "잇쿡4");
        ItCookUser user5 = createUser("user5@test.com", "잇쿡5");
        ItCookUser user6 = createUser("user6@test.com", "잇쿡6");

        createPosts(15, user1.getId());
        createPosts(GIBBAB_SECOND.getPostCount(), user2.getId());
        createPosts(GIBBAB_THIRD.getPostCount(), user3.getId());
        createPosts(GIBBAB_FOURTH.getPostCount(), user4.getId());
        createPosts(GIBBAB_FINAL.getPostCount(), user5.getId());

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then
        assertThat(jobExecution.getStatus()).isEqualTo(BatchStatus.COMPLETED);

        List<ItCookUser> users = userRepository.findAll();
        assertThat(users).hasSize(6)
            .extracting("badge")
            .containsExactly(GIBBAB_FIRST, GIBBAB_SECOND, GIBBAB_THIRD, GIBBAB_FOURTH,
                GIBBAB_FINAL,GIBBAB_FIRST)
        ;
    }

    private ItCookUser createUser(String username, String nickName) {
        ItCookUser user = ItCookUser.builder()
            .email(username)
            .password("cook1234")
            .providerType(ProviderType.COMMON)
            .nickName(nickName)
            .userRole(UserRole.USER)
            .build();

        return userRepository.save(user);
    }

    private void createPosts(int size, Long userId) {
        List<Post> posts = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            Post post = Post.builder()
                .recipeName("요리명")
                .introduction("소개글")
                .postImagePath("image1")
                .userId(userId)
                .postFlag(PostFlag.ACTIVATE)
                .build();
            posts.add(post);
        }
        postRepository.saveAll(posts);
    }
}