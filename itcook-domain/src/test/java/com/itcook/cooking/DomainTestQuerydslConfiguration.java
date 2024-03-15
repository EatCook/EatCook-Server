package com.itcook.cooking;


import com.itcook.cooking.domain.domains.post.repository.PostQuerydslRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@TestConfiguration
public class DomainTestQuerydslConfiguration {


    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory jpaQueryFactory() {
        return new JPAQueryFactory(entityManager);
    }

    @Bean
    public PostQuerydslRepository postQuerydslRepository() {
        return new PostQuerydslRepository(jpaQueryFactory());
    }

}
