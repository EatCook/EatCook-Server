package com.itcook.cooking.domain.domains.like.repository;

import com.itcook.cooking.domain.domains.like.entity.Liked;
import com.itcook.cooking.domain.domains.like.repository.dto.LikedDomainDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikedRepository extends JpaRepository<Liked, Long> {

    List<Liked> findAllByPostIdIn(List<Long> postIdData);

    @Query("SELECT new com.itcook.cooking.domain.domains.like.repository.dto.LikedDomainDto(" +
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
