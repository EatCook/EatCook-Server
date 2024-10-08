package com.itcook.cooking.domain.domains.post.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.itcook.cooking.domain.domains.IntegrationTestSupport;
import com.itcook.cooking.domain.domains.like.domain.repository.LikedRepository;
import com.itcook.cooking.domain.domains.like.domain.entity.Liked;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.domain.repository.PostQuerydslRepository;
import com.itcook.cooking.domain.domains.post.domain.repository.PostRepository;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.response.MyRecipeResponse;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.domain.enums.UserRole;
import com.itcook.cooking.domain.domains.user.domain.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class PostRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private LikedRepository likedRepository;

    @Autowired
    private PostQuerydslRepository postQuerydslRepository;

    @Test
    @DisplayName("해당 유저id의 글 목록(좋아요수를 포함한)을 조회한다.")
    void findPostsWithLiked() {
        //given

        ItCookUser user1 = createUser("user1@test.com", "잇쿡1");
        ItCookUser user2 = createUser("user2@test.com", "잇쿡2");

        Post post1 = createPost(user1.getId());
        Post post2 = createPost(user1.getId());

        createLike(user2.getId(), post2.getId());
        createLike(user1.getId(), post2.getId());
        //when
        Page<MyRecipeResponse> posts = postQuerydslRepository.findPostsWithLiked(
            user1.getId(), PageRequest.of(0, 10));

        //then
        assertThat(posts).hasSize(2)
            .extracting("postId","recipeName","likeCounts")
            .containsExactlyInAnyOrder(
                tuple(post2.getId(), post2.getRecipeName(), 2L),
                tuple(post1.getId(), post1.getRecipeName(), 0L)
            )
            ;

    }
    @Test
    @DisplayName("해당 유저id의 글들을 DISABLED 시킨다.")
    void updatePostToDisabled() {
        //given

        ItCookUser user = createUser("user@test.com", "잇쿡1");

        createPost(user.getId());
        createPost(user.getId());
        //when
        postRepository.updatePostToDisabled(user.getId());

        //then
        List<Post> posts = postRepository.findAll();
        assertThat(posts).hasSize(2)
            .extracting("postFlag")
            .containsExactly(PostFlag.DISABLED, PostFlag.DISABLED);
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


    private Post createPost(Long userId) {
        Post post = Post.builder()
            .recipeName("책제목")
            .introduction("소개글")
            .postImagePath("image1")
            .userId(userId)
            .postFlag(PostFlag.ACTIVATE)
            .build();
        return postRepository.save(post);
    }

    private void createLike(Long userId, Long postId) {
        Liked liked = Liked.builder()
            .postId(postId)
            .itCookUserId(userId)
            .build();
        likedRepository.save(liked);
    }

}
