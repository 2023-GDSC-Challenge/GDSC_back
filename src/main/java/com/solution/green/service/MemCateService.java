package com.solution.green.service;

import com.solution.green.dto.CategoryDto;
import com.solution.green.dto.MemCateDto;
import com.solution.green.entity.MemberCategory;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.CategoryRepository;
import com.solution.green.repository.MemCateRepository;
import com.solution.green.repository.MemDoRepository;
import com.solution.green.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.solution.green.code.GreenErrorCode.NO_CATEGORY;

@Service
@RequiredArgsConstructor
public class MemCateService {
    private final MemCateRepository memCateRepository;
    private final MemDoRepository memDoRepository;
    private final CategoryRepository categoryRepository;
    // TODO - 멤버가 카테고리에 대해 어떻게 평가했는지 get

    @Transactional
    public void getMemberCategory(Long memberId) {
        List<CategoryDto.Home> list = getCateHomeDtoList(memberId);
        for(CategoryDto.Home cate : list) {

        }
    }
    private Double setAchieveRate(Long categoryId) {
        return null;
    }
    @Transactional(readOnly = true)
    public Long getQuestNumPerCategory(Long categoryId) {
        return memDoRepository.countByQuest_SubCategory_Category(
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new GreenException(NO_CATEGORY))
        );
    }
    @Transactional(readOnly = true)
    private List<CategoryDto.Home> getCateHomeDtoList(Long memberId){
        return memCateRepository.findByMember_IdOrderByPriorityAsc(memberId)
                .stream()
                .map(CategoryDto.Home::fromEntity)
                .collect(Collectors.toList());
    }
}
