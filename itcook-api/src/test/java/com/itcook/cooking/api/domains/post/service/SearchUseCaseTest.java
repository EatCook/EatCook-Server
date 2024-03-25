package com.itcook.cooking.api.domains.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import com.itcook.cooking.api.domains.post.dto.response.SearchResultResponse;
import com.itcook.cooking.api.domains.post.dto.search.SearchPostProcess;
import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.LikedRepository;
import com.itcook.cooking.domain.domains.post.repository.PostRepository;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("test")
class SearchUseCaseTest {

    @Autowired
    private SearchUseCase searchUseCase;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LikedRepository likedRepository;


    @AfterEach
    void tearDown() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("ALTER TABLE post ALTER COLUMN post_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE itcook_user ALTER COLUMN user_id RESTART WITH 1").executeUpdate();
        em.getTransaction().commit();

        postRepository.deleteAll();
        userRepository.deleteAll();
        likedRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        Liked liked = new Liked(1L, 3L);
        Liked liked1 = new Liked(2L, 4L);
        Liked liked2 = new Liked(2L, 3L);

        likedRepository.saveAll(List.of(liked, liked1, liked2));

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
    @DisplayName("RecipeName 리스트를 받아서, RecipeName으로 구분지어서 검색 결과를 출력한다.")
//    @Transactional
    void search() {
        //given

        //when
        List<SearchResultResponse> result = searchUseCase
            .searchV3(null, List.of("test3","test2"),
            null, 10);
        //then
        assertThat(result).hasSize(2)
            .extracting("name")
            .contains("test3", "test2")
            ;
        List<SearchPostProcess> searchResults = result.get(0).getSearchResults();
        List<SearchPostProcess> searchResult2 = result.get(1).getSearchResults();

        assertThat(searchResults).hasSize(1)
            .extracting("recipeName")
            .contains(
                "test30"
            );
        assertThat(searchResults.get(0).getFoodIngredients()).hasSize(2)
                .contains("ingredient30", "ingredient31");
        assertThat(searchResult2).hasSize(9)
            .extracting("recipeName")
            .contains("test29", "test28", "test27", "test26", "test25", "test24", "test23", "test22", "test21")
        ;


    }

    @Test
    @DisplayName("RecipeName 리스트를 받아서, RecipeName으로 구분지어서 검색 결과를 출력한다. 두번째 페이지를 출력한다")
    void searchSecondPage() {
        //given

        //when
        List<SearchResultResponse> result = searchUseCase
            .searchV3(21L, List.of("test3","test2"),
            null, 10);
        //then
        assertThat(result).hasSize(2)
            .extracting("name")
            .contains("test3", "test2")
            ;
        List<SearchPostProcess> searchResults1 = result.get(0).getSearchResults();
        List<SearchPostProcess> searchResult2 = result.get(1).getSearchResults();

        assertThat(searchResults1).hasSize(1)
            .extracting("recipeName")
            .contains(
                "test3"
            );
        assertThat(searchResults1.get(0).getFoodIngredients()).hasSize(2)
                .contains("ingredient3", "ingredient4");
        assertThat(searchResult2).hasSize(2)
            .extracting("recipeName")
            .contains("test20","test2");
        assertThat(searchResult2.get(0).getFoodIngredients()).hasSize(2)
                .contains("ingredient20", "ingredient21");
        ;
    }

    @Test
    @DisplayName("RecipeName과 Ingredients가 null 값일시, 전체 최신순 조회한다.")
    void searchTwoParametersNull() {
        //given
        //when
        List<SearchResultResponse> result = searchUseCase
            .searchV3(null, null,
                null, 10);

        //then
        assertThat(result.get(0).getName()).isEqualTo("전체 검색");
        assertThat(result.get(0).getSearchResults()).hasSize(10)
            .extracting("recipeName")
            .containsExactly("test30", "test29", "test28", "test27", "test26", "test25", "test24", "test23", "test22", "test21");
        ;
    }
}