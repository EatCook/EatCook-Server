package com.itcook.cooking.domain.domains.post.domain.repository;

import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.entity.RecipeProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeProcessRepository extends JpaRepository<RecipeProcess, Long> {
    List<RecipeProcess> findByPost(Post post);
    void deleteAllByPostAndStepNumIn(Post post, List<Integer> stepNums);

}
