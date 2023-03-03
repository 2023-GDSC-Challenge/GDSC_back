package com.solution.green.service;

import com.solution.green.dto.CategoryDto;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.CategoryRepository;
import com.solution.green.repository.MemCateRepository;
import com.solution.green.repository.MemDoRepository;
import com.solution.green.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.solution.green.code.GreenErrorCode.NO_CATEGORY;

@Service
@RequiredArgsConstructor
public class MemCateService {
    private final MemCateRepository memCateRepository;
    private final MemDoRepository memDoRepository;
    private final CategoryRepository categoryRepository;
    private final QuestRepository questRepository;

    public List<CategoryDto.Home> getMemberCategoryHome(Long memberId) {
        List<CategoryDto.Home> list = getCateHomeDtoList(memberId);
        for (CategoryDto.Home cate : list)
            cate.setAchieveRate(setAchieveRateFromCateId(cate.getId()));
        return list;
    }

    private Double setAchieveRateFromCateId(Long categoryId) {
        Double doneCount = Double.valueOf(getDoneQuestPerCategory(categoryId));
        if (doneCount.equals(Double.valueOf(0))) return Double.valueOf(0);
        else
            return doneCount / Double.valueOf(getQuestNumPerCategory(categoryId)) * 100;
    }

    @Transactional(readOnly = true)
    public Long getDoneQuestPerCategory(Long categoryId) {
        return memDoRepository.countByQuest_SubCategory_CategoryAndStance(
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new GreenException(NO_CATEGORY)),
                2 // means done (0: 찜 | 1: ing)
        );
    }

    @Transactional(readOnly = true)
    public Long getQuestNumPerCategory(Long categoryId) {
        return questRepository.countBySubCategory_Category_Id(categoryId);
    }

    @Transactional(readOnly = true)
    private List<CategoryDto.Home> getCateHomeDtoList(Long memberId) {
        return memCateRepository.findByMember_IdOrderByPriorityAsc(memberId)
                .stream()
                .map(CategoryDto.Home::fromEntity)
                .collect(Collectors.toList());
    }
}
