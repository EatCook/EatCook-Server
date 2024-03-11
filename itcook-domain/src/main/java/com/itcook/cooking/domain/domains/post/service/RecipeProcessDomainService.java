package com.itcook.cooking.domain.domains.post.service;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.errorcode.RecipeProcessErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.entity.Post;
import com.itcook.cooking.domain.domains.post.entity.RecipeProcess;
import com.itcook.cooking.domain.domains.post.repository.RecipeProcessRepository;
import com.itcook.cooking.infra.s3.ImageUrlDto;
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

    public List<RecipeProcess> createRecipeProcess(List<RecipeProcess> recipeProcess) {
        recipeProcess.forEach((step) -> {
            int index = recipeProcess.indexOf(step) + 1;
            if (step.getStepNum() != index) {
                throw new ApiException(PostErrorCode.POST_REQUEST_ERROR);
            }
        });

        if (recipeProcess.isEmpty()) {
            throw new ApiException(RecipeProcessErrorCode.RECIPE_PROCESS_REQUEST_ERROR);
        }

        return recipeProcessRepository.saveAll(recipeProcess);
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
            if (newProcess.getStepNum() == null || newProcess.getRecipeWriting() == null) {
                throw new ApiException(RecipeProcessErrorCode.RECIPE_PROCESS_REQUEST_ERROR);
            }
            if (newProcess.getStepNum() != updateRecipeProcess.indexOf(newProcess) + 1) {
                throw new ApiException(RecipeProcessErrorCode.RECIPE_PROCESS_REQUEST_ERROR);
            }
        });

        List<RecipeProcess> existingRecipeProcess = recipeProcessRepository.findByPost(postData);

        for (RecipeProcess updateRecipeProcessData : updateRecipeProcess) {
            if (updateRecipeProcessData.getRecipeProcessImagePath() == null) {

                for (RecipeProcess existingRecipeProcessData : existingRecipeProcess) {
                    if (existingRecipeProcessData.getStepNum() == updateRecipeProcessData.getStepNum()) {
                        updateRecipeProcessData.updateFileExtension(existingRecipeProcessData.getRecipeProcessImagePath());
                    }
                }
            }
        }

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