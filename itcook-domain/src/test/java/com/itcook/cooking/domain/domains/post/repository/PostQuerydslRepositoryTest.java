package com.itcook.cooking.domain.domains.post.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.itcook.cooking.DomainTestQuerydslConfiguration;
import com.itcook.cooking.domain.domains.post.entity.Post;
import java.util.List;
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

    @BeforeEach
    void setUp() {
        postRepository.deleteAll();
        for (int i = 1; i <= 30; i++) {
            postRepository.save(
                Post.builder()
                    .recipeName("test"+i)
                    .foodIngredients(List.of("ingredient"+i, "ingredient"+(i+1)))   // 재료
                    .userId(1L)
                    .build()
            );
        }
    }

    @Test
    @DisplayName("nooffset 첫번쨰 페이지 테스트")
    void nooffset_firstPage_test() {
        //given

        //when
        List<Post> posts = postQuerydslRepository
            .findAllWithPagination(null, "test", null, 10L);

        //then
        assertEquals(10, posts.size());
        assertEquals("test30", posts.get(0).getRecipeName());
        assertEquals("test21", posts.get(9).getRecipeName());
    }

    @Test
    @DisplayName("nooffset 두번째 페이지 테스트")
    void nooffset_secondePage_test() {
        //given

        //when
        List<Post> posts = postQuerydslRepository
            .findAllWithPagination(21L, "test", null, 10L);

        //then
        assertEquals(10, posts.size());
        assertEquals("test20", posts.get(0).getRecipeName());
        assertEquals("test11", posts.get(9).getRecipeName());
    }

    @Test
    @DisplayName("재료 검색 테스트")
    void searchIngredientsTest() {
        //given

        //when
        List<Post> posts = postQuerydslRepository
            .findAllWithPagination(null, null, List.of("ingredient3", "ingredient2"), 10L);

        //then
        assertEquals(3, posts.size());
        assertEquals("test3", posts.get(0).getRecipeName());
        assertEquals("test1", posts.get(2).getRecipeName());
    }
}
