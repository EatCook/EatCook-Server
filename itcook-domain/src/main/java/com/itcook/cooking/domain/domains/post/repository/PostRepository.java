package com.itcook.cooking.domain.domains.post.repository;

import com.itcook.cooking.domain.domains.post.entity.Post;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByUserIdIn(List<Long> userId, Sort sort);


    List<Post> findAllByUserIdNot(Long userId, Sort sort);

}
