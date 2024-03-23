package com.itcook.cooking.api.domains.post.service;

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
class SearchUserCaseTest {

    @Autowired
    private SearchUserCase searchUserCase;

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
        em.getTransaction().commit();
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
    @DisplayName("post 테스트")
    @Transactional
    void test() {
        //given

        //when
        var searchNames = searchUserCase
            .searchV3(null, List.of("test3","test2"),
            null, 10);
        //then
        searchNames.forEach(System.out::println);

    }
}