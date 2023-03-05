package com.solution.green.service;

import com.solution.green.dto.CategoryDto;
import com.solution.green.dto.MemCateDto;
import com.solution.green.entity.Category;
import com.solution.green.entity.Member;
import com.solution.green.entity.MemberCategory;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.solution.green.code.GreenErrorCode.NO_CATEGORY;
import static com.solution.green.code.GreenErrorCode.NO_MEMBER;

@Service
@RequiredArgsConstructor
public class MemCateService {
    private final MemCateRepository memCateRepository;
    private final MemDoRepository memDoRepository;
    private final CategoryRepository categoryRepository;
    private final QuestRepository questRepository;
    private final MemberRepository memberRepository;

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
                getCategoryEntity(categoryId),
                1 // means done (0: ing | 1: done)
        );
    }

    @Transactional(readOnly = true)
    private Category getCategoryEntity(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GreenException(NO_CATEGORY));
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

    @Transactional(readOnly = true)
    public void createPriority(Long memberId, MemCateDto.Request request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new GreenException(NO_MEMBER));
        createMemberCategory(request.getFirst(), member, 1);
        createMemberCategory(request.getSecond(), member, 2);
        createMemberCategory(request.getThird(), member, 3);
        createMemberCategory(request.getFourth(), member, 4);
    }

    @Transactional
    private void createMemberCategory(Long categoryId,
                                      Member member,
                                      Integer priority) {
        memCateRepository.save(MemberCategory.builder()
                .member(member)
                .category(getCategoryEntity(categoryId))
                .priority(priority)
                .build());
    }

    @Transactional(readOnly = true)
    public void updatePriority(Long memberId, MemCateDto.Request request) {
        List<MemberCategory> prev =
                memCateRepository.findByMember_IdOrderByPriorityAsc(memberId);
        for (MemberCategory memberCategory : prev)
            if (memberCategory.getPriority().equals(1))
                updateMemberCategory(memberCategory, request.getFirst());
            else if (memberCategory.getPriority().equals(2))
                updateMemberCategory(memberCategory, request.getSecond());
            else if (memberCategory.getPriority().equals(3))
                updateMemberCategory(memberCategory, request.getThird());
            else if (memberCategory.getPriority().equals(4))
                updateMemberCategory(memberCategory, request.getFourth());
    }

    @Transactional
    private void updateMemberCategory(MemberCategory memberCategory, Long cateId) {
        memberCategory.setCategory(getCategoryEntity(cateId));
        memCateRepository.save(memberCategory);
    }
}
