package com.solution.green.service;

import com.solution.green.exception.GreenException;
import com.solution.green.repository.CategoryRepository;
import com.solution.green.repository.MemDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.solution.green.code.GreenErrorCode.NO_CATEGORY;

@Service
@RequiredArgsConstructor
public class MemDoService {
    private final MemDoRepository memDoRepository;
    private final CategoryRepository categoryRepository;
    @Transactional(readOnly = true)
    public Long getQuestNumPerCategory(Long categoryId) {
        return memDoRepository.countByQuest_SubCategory_Category(
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new GreenException(NO_CATEGORY))
        );
    }
}
