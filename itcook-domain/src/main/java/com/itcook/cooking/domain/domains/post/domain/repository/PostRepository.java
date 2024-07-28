package com.itcook.cooking.domain.domains.post.domain.repository;

import com.itcook.cooking.domain.domains.post.domain.enums.PostFlag;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p, u, pct, rp, l, a " +
            "FROM Post p " +
            "JOIN FETCH ItCookUser u ON p.userId = u.id " +
            "JOIN FETCH PostCookingTheme pct ON pct.post.id = p.id " +
            "JOIN FETCH RecipeProcess rp on rp.post.id = p.id " +
            "left JOIN FETCH Liked l on l.postId = p.id " +
            "left JOIN FETCH Archive a on a.postId = p.id " +
            "WHERE p.id = :postId AND p.postFlag = :postFlag")
    List<Object[]> findRecipeData(
            @Param("postId") Long postId, @Param("postFlag") PostFlag postFlag
    );

    Optional<Post> findByIdAndPostFlag(Long postId, PostFlag postFlag);
//
//    @Query(value = "select * from Post where post_id in (:postIds) order by field(p.id, :postIds)", nativeQuery = true)
//    List<Post> findByIdIn(List<Long> postIds);
}
