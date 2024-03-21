package com.itcook.cooking.domain.domains.post.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.itcook.cooking.DomainTestQuerydslConfiguration;
import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.dto.TestDto;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(DomainTestQuerydslConfiguration.class)
class PostQuerydslRepositoryTest {

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

    @AfterEach
    void tearDown() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("ALTER TABLE post ALTER COLUMN post_id RESTART WITH 1").executeUpdate();
        em.getTransaction().commit();
    }

    @BeforeEach
    void setUp() {
        ItCookUser user1 = userRepository.save(ItCookUser.builder()
            .nickName("잇쿡1")
            .email("user@test.com")
            .password("1234")
            .providerType(ProviderType.COMMON)
            .userRole(UserRole.USER)
            .build());
        ItCookUser user2 = userRepository.save(ItCookUser.builder()
            .nickName("잇쿡2")
            .email("user@test2.com")
            .password("1234")
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
    @DisplayName("Post가 삭제된 상태 조회 X")
    void not_search_disabled() {
        //given
        Post post = Post.builder()
            .recipeName("test0")
            .foodIngredients(Arrays.asList("ingredient1", "ingredient2"))
            .userId(1L)
            .postFlag(PostFlag.DISABLED)
            .build();
        postRepository.save(post);

        //when
        List<Post> posts = postQuerydslRepository.findNamesWithPagination(null, List.of("test0"),
            10);

        //then
        assertEquals(0, posts.size());
    }


    @Test
    @DisplayName("아무 검색을 하지 않았을때 전체 조회 테스트")
    void no_search_words_test() {
        //given
        //when
        List<Post> posts = postQuerydslRepository.findNamesWithPagination(null, null, 10);

        //then
        assertEquals(10, posts.size());
        assertEquals("test30", posts.get(0).getRecipeName());
    }

    @Test
    @DisplayName("nooffset 첫번쨰 페이지 테스트")
    void nooffset_firstPage_test() {
        //given


        //when
        List<Post> posts = postQuerydslRepository
            .findNamesWithPagination(null, List.of("test"),10);

        //then
        assertEquals(10, posts.size());
        assertEquals("test30", posts.get(0).getRecipeName());
        assertEquals("test21", posts.get(9).getRecipeName());
    }

    @Test
    @DisplayName("nooffset 첫번쨰 페이지 제목 contains 테스트")
    void nooffset_recipenNames_test() {
        //given


        //when
        List<Post> posts = postQuerydslRepository
            .findNamesWithPagination(null, List.of("test1"),10);

        //then
        assertEquals(10, posts.size());
        assertEquals("test19", posts.get(0).getRecipeName());
        assertEquals("test10", posts.get(9).getRecipeName());
    }

    @Test
    @DisplayName("nooffset 첫번쨰 페이지 재료 contains 테스트")
    void nooffset_ingredients_test() {
        //given


        //when
        List<Post> posts = postQuerydslRepository
            .findNamesWithPagination(null, List.of("ingredient2"),10);

        //then
        assertEquals(10, posts.size());
        assertEquals("test29", posts.get(0).getRecipeName());
        assertEquals("test20", posts.get(9).getRecipeName());
    }

    @Test
    @DisplayName("nooffset 두번째 페이지 제목 contains 테스트")
    void nooffset_second_recipenNames_test() {
        //given
        List<Post> all = postRepository.findAll();
        all.forEach(post -> System.out.println("post = " + post.getId()));

        //when
        List<Post> posts = postQuerydslRepository
            .findNamesWithPagination(10L, List.of("test1"),10);

        //then
        assertEquals(1, posts.size());
        assertEquals("test1", posts.get(0).getRecipeName());
    }

    @Test
    @DisplayName("nooffset 두번째 페이지 테스트")
    void nooffset_secondePage_test() {
        //given
        List<Post> all = postRepository.findAll();
        all.forEach(post -> System.out.println("post = " + post.getId()));

        //when
        List<Post> posts = postQuerydslRepository
            .findNamesWithPagination(21L, List.of("test"), 10);

        //then
        assertEquals(10, posts.size());
        assertEquals("test20", posts.get(0).getRecipeName());
        assertEquals("test11", posts.get(9).getRecipeName());
    }

    @Test
    @DisplayName("재료 조회 테스트")
    void findAllWithRecipeNamesTest() {
        //given
//        List<SearchIngredients> ingredients = postQuerydslRepository
//            .findAllWithPagination(null, List.of("test1"), null,10);
        //when
//        ingredients.forEach(searchIngredients -> System.out.println("searchIngredients = " + searchIngredients));

        //then
    }

    @Test
    @DisplayName("RecipeName 조회 테스트")
    void findAllWithIngredientsTest() {
        //given
        Liked liked = new Liked(1L, 30L);
        Liked liked2 = new Liked(2L, 30L);
        Liked liked3 = new Liked(2L, 3L);
        Liked liked1 = new Liked(2L, 4L);

        likedRepository.saveAll(List.of(liked, liked1, liked2, liked3));

        var ingredients = postQuerydslRepository
            .findAllWithPagination(null, null, null,10);
        //when
        ingredients.forEach(searchNames -> System.out.println("searchIngredients = " + searchNames));



        //then
    }

//    @Test
//    @DisplayName("Post와 LikeCount 조회 테스트")
//    void testLiked() {
//        //given
//        Liked liked = new Liked(1L, 3L);
//        Liked liked1 = new Liked(2L, 4L);
//        Liked liked2 = new Liked(2L, 3L);
//
//        likedRepository.saveAll(List.of(liked, liked1, liked2));
//
//        //when
//        List<TestDto> postWithLikes = postQuerydslRepository.findPostsWithLikes();
//        postWithLikes.forEach(testDto -> System.out.println("testDto = " + testDto));
//
//        //then
//    }
}
