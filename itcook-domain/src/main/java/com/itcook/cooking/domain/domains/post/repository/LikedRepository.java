package com.itcook.cooking.domain.domains.post.repository;

import com.itcook.cooking.domain.domains.post.entity.Liked;
import com.itcook.cooking.domain.domains.post.repository.dto.LikedDomainDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Objects;

public interface LikedRepository extends JpaRepository<Liked, Long> {

    List<Liked> findAllByPostIdIn(List<Long> postIdData);

    @Query("SELECT new com.itcook.cooking.domain.domains.post.repository.dto.LikedDomainDto(" +
            "p, u, l) " +
            "FROM ItCookUser u " +
            "LEFT JOIN FETCH Post p ON p.id = :postId " +
            "LEFT JOIN FETCH Liked l ON l.postId = p.id AND l.itCookUserId = u.id " +
            "WHERE u.id = :userId")
    LikedDomainDto findUserWithPostAndArchiveById(
            @Param("userId") Long userId,
            @Param("postId") Long postId
    );

}
