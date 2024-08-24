package com.itcook.cooking.domain.domains.post.service;

import com.itcook.cooking.domain.domains.post.domain.adaptor.PostAdaptor;
import com.itcook.cooking.domain.domains.post.domain.entity.PostImageRegisterService;
import com.itcook.cooking.domain.domains.post.domain.entity.validator.PostValidator;
import com.itcook.cooking.domain.domains.post.domain.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class RecipeProcessService {
    private final PostRepository postRepository;
    private final PostImageRegisterService postImageRegisterService;
    private final PostAdaptor postAdaptor;
    private final PostValidator postValidator;
}
