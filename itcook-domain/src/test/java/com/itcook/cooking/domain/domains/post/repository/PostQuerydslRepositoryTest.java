package com.itcook.cooking.domain.domains.post.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.itcook.cooking.domain.domains.IntegrationTestSupport;
import com.itcook.cooking.domain.domains.like.repository.LikedRepository;
import com.itcook.cooking.domain.domains.like.entity.Liked;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.dto.SearchPostDto;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
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

//    @Test
//    @DisplayName("Post가 삭제된 상태 조회 X")
//    void not_search_disabled() {
//        //given
//        Post post = Post.builder()
//            .recipeName("test0")
//            .foodIngredients(Arrays.asList("ingredient1", "ingredient2"))
//            .userId(1L)
//            .postFlag(PostFlag.DISABLED)
//            .build();
//        postRepository.save(post);
//
//        //when
//        List<Post> posts = postQuerydslRepository.findNamesWithPagination(null, List.of("test0"),
//            10);
//
//        //then
//        assertEquals(0, posts.size());
//    }
//
//
//    @Test
//    @DisplayName("아무 검색을 하지 않았을때 전체 조회 테스트")
//    void no_search_words_test() {
//        //given
//        //when
//        List<Post> posts = postQuerydslRepository.findNamesWithPagination(null, null, 10);
//
//        //then
//        assertEquals(10, posts.size());
//        assertEquals("test30", posts.get(0).getRecipeName());
//    }
//
//    @Test
//    @DisplayName("nooffset 첫번쨰 페이지 테스트")
//    void nooffset_firstPage_test() {
//        //given
//
//
//        //when
//        List<Post> posts = postQuerydslRepository
//            .findNamesWithPagination(null, List.of("test"),10);
//
//        //then
//        assertEquals(10, posts.size());
//        assertEquals("test30", posts.get(0).getRecipeName());
//        assertEquals("test21", posts.get(9).getRecipeName());
//    }

//    @Test
//    @DisplayName("nooffset 첫번쨰 페이지 제목 contains 테스트")
//    void nooffset_recipenNames_test() {
//        //given
//
//
//        //when
//        List<Post> posts = postQuerydslRepository
//            .findNamesWithPagination(null, List.of("test1"),10);
//
//        //then
//        assertEquals(10, posts.size());
//        assertEquals("test19", posts.get(0).getRecipeName());
//        assertEquals("test10", posts.get(9).getRecipeName());
//    }
//
//    @Test
//    @DisplayName("nooffset 첫번쨰 페이지 재료 contains 테스트")
//    void nooffset_ingredients_test() {
//        //given
//
//
//        //when
//        List<Post> posts = postQuerydslRepository
//            .findNamesWithPagination(null, List.of("ingredient2"),10);
//
//        //then
//        assertEquals(10, posts.size());
//        assertEquals("test29", posts.get(0).getRecipeName());
//        assertEquals("test20", posts.get(9).getRecipeName());
//    }
//
//    @Test
//    @DisplayName("nooffset 두번째 페이지 제목 contains 테스트")
//    void nooffset_second_recipenNames_test() {
//        //given
//        List<Post> all = postRepository.findAll();
//        all.forEach(post -> System.out.println("post = " + post.getId()));
//
//        //when
//        List<Post> posts = postQuerydslRepository
//            .findNamesWithPagination(10L, List.of("test1"),10);
//
//        //then
//        assertEquals(1, posts.size());
//        assertEquals("test1", posts.get(0).getRecipeName());
//    }
//
//    @Test
//    @DisplayName("nooffset 두번째 페이지 테스트")
//    void nooffset_secondePage_test() {
//        //given
//        List<Post> all = postRepository.findAll();
//        all.forEach(post -> System.out.println("post = " + post.getId()));
//
//        //when
//        List<Post> posts = postQuerydslRepository
//            .findNamesWithPagination(21L, List.of("test"), 10);
//
//        //then
//        assertEquals(10, posts.size());
//        assertEquals("test20", posts.get(0).getRecipeName());
//        assertEquals("test11", posts.get(9).getRecipeName());
//    }

//    @Test
//    @DisplayName("RecipeName과 IngredientName이 null로 넘어올시 Post를 전체 조회한다.")
//    void findAllWithIngredientsWithNull() {
//        //given
//
//        //when
//        List<Post> posts = postQuerydslRepository
//            .findNamesWithPagination(null, null, 10);
//
//        //then
//        assertThat(posts).hasSize(10)
//            .extracting("recipeName")
//            .containsExactlyInAnyOrder("test30", "test29", "test28", "test27", "test26", "test25", "test24", "test23", "test22", "test21")
//            ;
//    }
//
//    @Test
//    @DisplayName("RecipeName을 리스트로 입력받아 첫번째 페이지 Post를 조회한다.")
//    void findAllWithRecipeNames() {
//        //given
//
//        //when
//        var ingredients = postQuerydslRepository
//            .findAllWithPagination(null, List.of("test2","test3"), null,10);
//
//
//        //then
//        assertThat(ingredients).hasSize(10)
//            .extracting("recipeName")
//            .containsExactlyInAnyOrder("test30", "test29", "test28", "test27", "test26", "test25", "test24", "test23", "test22", "test21")
//            ;
//    }
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
