package com.itcook.cooking.domain.domains.post.service;

import com.itcook.cooking.domain.common.errorcode.PostErrorCode;
import com.itcook.cooking.domain.common.errorcode.RecipeProcessErrorCode;
import com.itcook.cooking.domain.common.exception.ApiException;
import com.itcook.cooking.domain.domains.post.domain.entity.Post;
import com.itcook.cooking.domain.domains.post.domain.entity.RecipeProcess;
import com.itcook.cooking.domain.domains.post.domain.repository.RecipeProcessRepository;
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
public class RecipeProcessService {

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


    public void deleteRecipeProcess(Post post, List<RecipeProcess> recipeProcessesData) {
        List<Integer> stepNumData = recipeProcessesData.stream().map(RecipeProcess::getStepNum).collect(Collectors.toList());
        recipeProcessRepository.deleteAllByPostAndStepNumIn(post, stepNumData);
    }

    public void updateRecipeProcess(List<RecipeProcess> updateRecipeProcess, Post postData) {
        // 유효성 검사를 먼저 수행
        recipeProcessNullValidation(updateRecipeProcess);

        // 기존 레시피 과정 로드
        List<RecipeProcess> existingRecipeProcess = recipeProcessRepository.findByPost(postData);

        // 이미지 파일 확장자 업데이트
        recipeProcessImageFileExtensionUpdate(updateRecipeProcess, existingRecipeProcess);

        // 변경된 레시피 과정 업데이트
        recipeProcessUpdateValidation(updateRecipeProcess, existingRecipeProcess);

        // 새로운 과정 추가 또는 삭제 처리
        handleCreateOrDeletedProcesses(updateRecipeProcess, postData, existingRecipeProcess);
    }

    private static void recipeProcessNullValidation(List<RecipeProcess> updateRecipeProcess) {
        for (RecipeProcess newProcess : updateRecipeProcess) {
            if (newProcess.getStepNum() == null || newProcess.getRecipeWriting() == null ||
                    newProcess.getStepNum() != updateRecipeProcess.indexOf(newProcess) + 1) {
                throw new ApiException(RecipeProcessErrorCode.RECIPE_PROCESS_REQUEST_ERROR);
            }
        }
    }

    private static void recipeProcessImageFileExtensionUpdate(List<RecipeProcess> updateRecipeProcess, List<RecipeProcess> existingRecipeProcess) {
        for (RecipeProcess updateProcess : updateRecipeProcess) {
            if (updateProcess.getRecipeProcessImagePath() == null) {
                existingRecipeProcess.stream()
                        .filter(existingProcess -> existingProcess.getStepNum().equals(updateProcess.getStepNum()))
                        .findFirst()
                        .ifPresent(existingProcess -> updateProcess.updateFileExtension(existingProcess.getRecipeProcessImagePath()));
            }
        }
    }

    private static void recipeProcessUpdateValidation(List<RecipeProcess> updateRecipeProcess, List<RecipeProcess> existingRecipeProcess) {
        for (RecipeProcess newProcess : updateRecipeProcess) {
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
        }
    }

    private void handleCreateOrDeletedProcesses(List<RecipeProcess> updateRecipeProcess, Post postData, List<RecipeProcess> existingRecipeProcess) {
        int newSize = updateRecipeProcess.size();
        int existingSize = existingRecipeProcess.size();
        if (newSize > existingSize) {
            createRecipeProcess(updateRecipeProcess.subList(existingSize, newSize));
        } else if (newSize < existingSize) {
            deleteRecipeProcess(postData, existingRecipeProcess.subList(newSize, existingSize));
        }
    }

}