package com.itcook.cooking.domain.domains.post.repository;

import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.entity.RecipeProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RecipeProcessRepository extends JpaRepository<RecipeProcess, Long> {
    List<RecipeProcess> findByPost(Post post);
    void deleteAllByPostAndStepNumIn(Post post, List<Integer> stepNums);

}
