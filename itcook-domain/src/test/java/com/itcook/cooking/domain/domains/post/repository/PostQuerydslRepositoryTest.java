package com.itcook.cooking.domain.domains.post.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.itcook.cooking.domain.domains.IntegrationTestSupport;
import com.itcook.cooking.domain.domains.like.domain.entity.Liked;
import com.itcook.cooking.domain.domains.like.domain.repository.LikedRepository;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.domain.repository.PostQuerydslRepository;
import com.itcook.cooking.domain.domains.post.domain.repository.PostRepository;
import com.itcook.cooking.domain.domains.post.domain.repository.dto.SearchPostDto;
import com.itcook.cooking.domain.domains.user.domain.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.domain.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.domain.enums.UserRole;
import com.itcook.cooking.domain.domains.user.domain.repository.UserRepository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Transactional
class PostQuerydslRepositoryTest extends IntegrationTestSupport {

    @Autowired
    private PostQuerydslRepository postQuerydslRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikedRepository likedRepository;

    @Autowired
    private EntityManagerFactory entityManagerFactory;


    @BeforeEach
    void setUp() {

        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("ALTER TABLE post ALTER COLUMN post_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE itcook_user ALTER COLUMN user_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE liked ALTER COLUMN liked_id RESTART WITH 1").executeUpdate();
        em.getTransaction().commit();

        ItCookUser user1 = userRepository.save(ItCookUser.builder()
            .nickName("잇쿡1")
            .email("user@test.com")
            .password("cook1234")
            .providerType(ProviderType.COMMON)
            .userRole(UserRole.USER)
            .build());
        ItCookUser user2 = userRepository.save(ItCookUser.builder()
            .nickName("잇쿡2")
            .email("user@test2.com")
            .password("cook1234")
            .providerType(ProviderType.COMMON)
            .userRole(UserRole.USER)
            .build());

        for (int i = 1; i <= 30; i++) {
            if (i <= 15) {
                postRepository.save(
                    Post.builder()
                        .recipeName("test"+i)
                        .foodIngredients(List.of("ingredient"+i, "ingredient"+(i+1)))   // 재료
                        .userId(user1.getId())
                        .postFlag(PostFlag.ACTIVATE)
                        .build()
                );
            } else {
                postRepository.save(
                    Post.builder()
                        .recipeName("test"+i)
                        .foodIngredients(List.of("ingredient"+i, "ingredient"+(i+1)))   // 재료
                        .userId(user2.getId())
                        .postFlag(PostFlag.ACTIVATE)
                        .build()
                );

            }
        }

    }

    @Test
    @DisplayName("RecipeName을 리스트로 입력받아 두번쨰 페이지 Post를 조회한다.")
    void findAllWithRecipeNamesSecondPage() {
        //given

        //when
        var ingredients = postQuerydslRepository
            .findAllWithPagination(21L, List.of("test2","test3"), null,10);


        //then
        assertThat(ingredients).hasSize(3)
            .extracting("recipeName")
            .containsExactlyInAnyOrder("test20", "test3", "test2")
            ;
    }

    @Test
    @DisplayName("IngredientName을 리스트로 입력받아 첫번째 페이지 Post를 조회한다.")
    void findAllWithIngredients() {
        //given

        //when
        var ingredients = postQuerydslRepository
            .findAllWithPagination(null, null, List.of("ingredient1"),10);

        //then
        assertThat(ingredients).hasSize(10)
            .extracting("recipeName")
            .containsExactlyInAnyOrder("test19", "test18", "test17", "test16", "test15", "test14", "test13", "test12", "test11", "test10")
            ;
    }

    @Test
    @DisplayName("IngredientName을 리스트로 입력받아 두번째 페이지 Post를 조회한다.")
    void findAllWithIngredientsSecondPage() {
        //given

        //when
        var ingredients = postQuerydslRepository
            .findAllWithPagination(10L, null, List.of("ingredient1"),10);

        //then
        assertThat(ingredients).hasSize(2)
            .extracting("recipeName")
            .containsExactlyInAnyOrder("test9", "test1")
            ;
    }

    @Test
    @DisplayName("전체 조회")
    void findAll() {
        //given
        List<SearchPostDto> posts = postQuerydslRepository.findAllWithPagination(null,
            List.of("test3"), null, 10);
        //when

        //then
        posts.forEach(System.out::println);
    }

    @Test
    @DisplayName("RecipeName 리스트를 받고 Post를 조회할시, 좋아요수도 조회한다")
    void findAllWithIngredientsLike() {
        //given
        Liked liked = new Liked(1L, 30L);
        Liked liked2 = new Liked(2L, 30L);
        Liked liked3 = new Liked(2L, 29L);
        Liked liked1 = new Liked(2L, 28L);

        likedRepository.saveAll(List.of(liked, liked1, liked2, liked3));

        //when
        var ingredients = postQuerydslRepository
            .findAllWithPagination(null, List.of("test2","test3"), null,10);

        //then
        assertThat(ingredients)
            .extracting("likeCount")
            .containsExactlyInAnyOrder(2L, 1L, 1L, 0L, 0L, 0L, 0L, 0L, 0L, 0L);


    }

}
