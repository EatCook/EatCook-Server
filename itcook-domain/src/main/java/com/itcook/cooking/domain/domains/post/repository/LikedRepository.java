package com.itcook.cooking.domain.domains.post.repository;

import com.itcook.cooking.domain.domains.post.entity.Liked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikedRepository extends JpaRepository<Liked, Long> {

    List<Liked> findAllByPostIdIn(List<Long> postIdData);

    @Query("SELECT u, p, l " +
            "FROM ItCookUser u " +
            "LEFT JOIN FETCH Post p ON p.id = :postId " +
            "LEFT JOIN FETCH Liked l ON l.postId = p.id AND l.itCookUserId = u.id " +
            "WHERE u.id = :userId")
    List<Object[]> findUserWithPostAndArchiveById(
            @Param("userId") Long userId,
            @Param("postId") Long postId
    );

}
