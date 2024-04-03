package com.itcook.cooking.api.domains.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

import com.itcook.cooking.api.IntegrationMockTestSupport;
import com.itcook.cooking.api.domains.post.dto.search.SearchPostResponse;
import com.itcook.cooking.api.domains.post.service.dto.PostSearchServiceDto;
import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.repository.LikedRepository;
import com.itcook.cooking.domain.domains.post.repository.PostRepository;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import com.itcook.cooking.domain.domains.user.repository.UserRepository;
import com.itcook.cooking.infra.redis.RealTimeSearchWords;
import com.itcook.cooking.infra.redis.SearchWordsEventListener;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

//@SpringBootTest
//@ActiveProfiles("test")
class SearchUseCaseTest extends IntegrationMockTestSupport {

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

    @MockBean
    private SearchWordsEventListener searchWordsEventListener;


    @AfterEach
    void tearDown() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.createNativeQuery("ALTER TABLE post ALTER COLUMN post_id RESTART WITH 1").executeUpdate();
        em.createNativeQuery("ALTER TABLE itcook_user ALTER COLUMN user_id RESTART WITH 1").executeUpdate();
        em.getTransaction().commit();

        postRepository.deleteAllInBatch();
        userRepository.deleteAllInBatch();
        likedRepository.deleteAllInBatch();
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
    @DisplayName("RecipeName 리스트를 받아서 첫번째 페이지의 검색 결과를 출력한다.")
    @Transactional
    void search() {
        //given
        doNothing().when(searchWordsEventListener).handle(any(RealTimeSearchWords.class));
        PostSearchServiceDto serviceDto = PostSearchServiceDto.builder()
            .lastId(null)
            .recipeNames(List.of("test3"))
            .ingredients(null)
            .size(10)
            .build();

        //when
        List<SearchPostResponse> result = searchUseCase
            .searchV4(serviceDto);

        //then
        assertThat(result).hasSize(2)
            .extracting("recipeName")
            .containsExactly("test30", "test3");
        ;
    }

    @Test
    @DisplayName("RecipeName 리스트를 받아서 두번째 페이지의 검색 결과를 출력한다.")
    @Transactional
    void searchSecondPage() {
        //given
        doNothing().when(searchWordsEventListener).handle(any(RealTimeSearchWords.class));

        PostSearchServiceDto serviceDto = PostSearchServiceDto.builder()
            .lastId(21L)
            .recipeNames(List.of("test3","test2"))
            .ingredients(null)
            .build();

        //when
        List<SearchPostResponse> result = searchUseCase
            .searchV4(serviceDto);

        //then
        assertThat(result).hasSize(3)
            .extracting("recipeName")
            .containsExactly("test20", "test3","test2");
        ;
    }
    @Test
    @DisplayName("입력 결과가 null 일때, 전체 검색 결과를 출력한다.")
    @Transactional
    void searchAllWithNull() {
        //given
        doNothing().when(searchWordsEventListener).handle(any(RealTimeSearchWords.class));
        PostSearchServiceDto serviceDto = PostSearchServiceDto.builder()
            .lastId(null)
            .recipeNames(null)
            .ingredients(null)
            .build();

        //when
        List<SearchPostResponse> result = searchUseCase
            .searchV4(serviceDto);

        //then
        assertThat(result).hasSize(10)
            .extracting("recipeName")
            .contains("test30", "test29", "test28", "test27", "test26", "test25", "test24", "test23", "test22", "test21")
        ;
    }

}