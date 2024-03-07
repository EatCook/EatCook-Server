package com.itcook.cooking.api;

import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.user.entity.Archive;
import com.itcook.cooking.domain.domains.user.entity.ItCookUser;
import com.itcook.cooking.domain.domains.user.enums.ProviderType;
import com.itcook.cooking.domain.domains.user.enums.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;


    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit1() {

            save(ItCookUser.builder()
                    .userRole(UserRole.USER)
                    .nickName("유저1")
                    .password("123")
                    .email("test1@naver.com")
                    .providerType(ProviderType.COMMON)
                    .follow(new ArrayList<>(Arrays.asList(2L, 3L)))
                    .likeds(new ArrayList<>(Arrays.asList(5L)))
                    .build());

            save(ItCookUser.builder()
                    .userRole(UserRole.USER)
                    .nickName("유저2")
                    .password("123")
                    .email("test2@naver.com")
                    .providerType(ProviderType.COMMON)
                    .follow(new ArrayList<>(Arrays.asList(1L, 3L)))
                    .likeds(new ArrayList<>(Arrays.asList(5L)))
                    .build());

            save(ItCookUser.builder()
                    .userRole(UserRole.USER)
                    .nickName("유저3")
                    .password("123")
                    .email("test3@naver.com")
                    .providerType(ProviderType.COMMON)
                    .build());

            save(ItCookUser.builder()
                    .userRole(UserRole.USER)
                    .nickName("유저4")
                    .password("123")
                    .email("test4@naver.com")
                    .providerType(ProviderType.COMMON)
                    .build());

            save(Post.builder()
                    .recipeName("첫 번째 메뉴")
                    .recipeTime(10)
                    .introduction("첫 번째 본문")
                    .userId(1L).likeCount(1)
                    .foodIngredients(new ArrayList<>(Arrays.asList("첫 번째", "첫번째"))
                    )
                    .postFlag((byte) 0).build()
            );
            save(Post.builder()
                    .recipeName("두 번째 메뉴")
                    .recipeTime(10)
                    .introduction("두 번째 본문")
                    .userId(2L)
                    .likeCount(1)
                    .foodIngredients(new ArrayList<>(Arrays.asList("두 번째", "두번째"))
                    )
                    .postFlag((byte) 0).build()
            );
            save(Post.builder()
                    .recipeName("세 번째 메뉴")
                    .recipeTime(10)
                    .introduction("세 번째 본문")
                    .userId(3L)
                    .likeCount(1)
                    .foodIngredients(new ArrayList<>(Arrays.asList("세 번째", "세번째"))
                    )
                    .postFlag((byte) 0).build()
            );
            save(Post.builder()
                    .recipeName("네 번째 메뉴")
                    .recipeTime(10)
                    .introduction("네 번째 본문")
                    .userId(4L)
                    .likeCount(1)
                    .foodIngredients(new ArrayList<>(Arrays.asList("네 번째", "네번째"))
                    )
                    .postFlag((byte) 0).build()
            );

            save(Archive.builder()
                    .itCookUserId(1L)
                    .postId(5L)
                    .build());

            save(Archive.builder()
                    .itCookUserId(2L)
                    .postId(5L)
                    .build());
        }

        public void save(Object... objects) {
            for (Object object : objects) {
                em.persist(object);
            }
        }
    }
}
