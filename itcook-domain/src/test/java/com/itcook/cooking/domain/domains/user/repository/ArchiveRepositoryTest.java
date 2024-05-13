package com.itcook.cooking.domain.domains.user.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.itcook.cooking.domain.domains.IntegrationTestSupport;
import com.itcook.cooking.domain.domains.archive.dto.ArchivePost;
import com.itcook.cooking.domain.domains.archive.repository.ArchiveQuerydslRepository;
import com.itcook.cooking.domain.domains.archive.repository.ArchiveRepository;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.PostRepository;
import com.itcook.cooking.domain.domains.archive.entity.Archive;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.enums.UserState;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class ArchiveRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private ArchiveRepository archiveRepository;
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ArchiveQuerydslRepository archiveQuerydslRepository;

    @Test
    @DisplayName("List의 PostIds로 받기")
    void findPostIdsByUserId() {
        //given
        ItCookUser user = createUser("user@test.com", "잇쿡1");
        ItCookUser user2 = createUser("user2@test.com", "잇쿡2");

        Post post1 = createPost("요리1", user.getId());
        Post post2 = createPost("요리2", user.getId());
        Post post3 = createPost("요리3", user.getId());

        createArchive(post1.getId(), user2.getId());
        createArchive(post2.getId(), user2.getId());
        createArchive(post3.getId(), user2.getId());


        //when
        List<ArchivePost> archivePosts = archiveQuerydslRepository.findPostsByUserId(
            user2.getId());

        //then
        List<Long> postIds = archivePosts.stream().map(ArchivePost::getPostId).toList();
        assertThat(postIds).hasSize(3)
            .containsExactly(post3.getId(),post2.getId(),post1.getId())
            ;


    }



    private ItCookUser createUser(String username, String nickName) {
        ItCookUser user = ItCookUser.builder()
            .email(username)
            .password("cook12345")
            .providerType(ProviderType.COMMON)
            .nickName(nickName)
//            .userState(UserState.ACTIVE)
            .userRole(UserRole.USER)
            .build();

        return userRepository.save(user);
    }

    private Post createPost(String recipeName, Long userId) {
        Post post = Post.builder()
            .recipeName(recipeName)
            .userId(userId)
            .postFlag(PostFlag.ACTIVATE)
            .build();

        return postRepository.save(post);
    }


    private Archive createArchive(Long postId, Long userId) {
        Archive archive = Archive.builder()
            .postId(postId)
            .itCookUserId(userId)
            .build();

        return archiveRepository.save(archive);
    }
}