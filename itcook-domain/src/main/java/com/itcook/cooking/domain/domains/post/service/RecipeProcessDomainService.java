package com.itcook.cooking.domain.domains.post.service;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.errorcode.RecipeProcessErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.entity.RecipeProcess;
import com.itcook.cooking.domain.domains.post.repository.RecipeProcessRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RecipeProcessDomainService {

    private final RecipeProcessRepository recipeProcessRepository;

    public void createRecipeProcess(List<RecipeProcess> recipeProcess) {
        if (recipeProcess.isEmpty()) {
            throw new ApiException(RecipeProcessErrorCode.RECIPE_PROCESS_REQUEST_ERROR);
        }

        recipeProcessRepository.saveAll(recipeProcess);
    }

    public List<RecipeProcess> readRecipeProcess(Post post) {

        List<RecipeProcess> findPost = recipeProcessRepository.findByPost(post);

        if (findPost.isEmpty()) {
            throw new ApiException(PostErrorCode.POST_NOT_EXIST);
        }

        return findPost;
    }

    public void deleteRecipeProcess(Post post, List<RecipeProcess> recipeProcessesData) {
        List<Integer> stepNumData = recipeProcessesData.stream().map(RecipeProcess::getStepNum).collect(Collectors.toList());
        recipeProcessRepository.deleteAllByPostAndStepNumIn(post, stepNumData);
    }

    public void updateRecipeProcess(List<RecipeProcess> updateRecipeProcess, Post postData) {
        updateRecipeProcess.forEach(newProcess -> {
            if (newProcess.getStepNum() != updateRecipeProcess.indexOf(newProcess) + 1) {
                throw new ApiException(RecipeProcessErrorCode.RECIPE_PROCESS_REQUEST_ERROR);
            }
        });

        List<RecipeProcess> existingRecipeProcess = recipeProcessRepository.findByPost(postData);

        updateRecipeProcess.forEach(newProcess -> {
            existingRecipeProcess.stream()
                    .filter(oldProcess -> newProcess.getStepNum().equals(oldProcess.getStepNum()))
                    .findFirst()
                    .ifPresent(oldProcess -> {
                        boolean writingChanged = !newProcess.getRecipeWriting().equals(oldProcess.getRecipeWriting());
                        boolean imagePathChanged = !newProcess.getRecipeProcessImagePath().equals(oldProcess.getRecipeProcessImagePath());

                        if (writingChanged || imagePathChanged) {
                            oldProcess.updateRecipeProcess(newProcess);
                        }
                    });
        });

        // 새로운 레시피 과정이 추가되거나 삭제된 경우에 대한 처리
        List<RecipeProcess> recipeProcesses;
        if (updateRecipeProcess.size() > existingRecipeProcess.size()) {
            recipeProcesses = updateRecipeProcess.stream()
                    .skip(existingRecipeProcess.size())
                    .collect(Collectors.toList());
            createRecipeProcess(recipeProcesses);
        } else {
            recipeProcesses = existingRecipeProcess.stream()
                    .skip(updateRecipeProcess.size())
                    .collect(Collectors.toList());
            deleteRecipeProcess(postData, recipeProcesses);
        }
    }

}