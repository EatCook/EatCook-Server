package com.itcook.cooking.domain.domains.post.domain.repository;

import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.enums.PostFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post> findByIdAndPostFlag(Long postId, PostFlag postFlag);

}
