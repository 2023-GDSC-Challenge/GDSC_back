package com.solution.green.service;

import com.solution.green.dto.MemberDto;
import com.solution.green.entity.Member;
import com.solution.green.entity.SubCategories;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.MemberRepository;
import com.solution.green.repository.SubCateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.solution.green.code.GreenErrorCode.ALREADY_REGISTERED;

@Service
@RequiredArgsConstructor
public class QuestService {
    private final SubCateRepository subCateRepository;

    // TODO - 각 카테고리별로 몇개 있는지 카운트하는 함수
    @Transactional
    public void getQuestNumPerCategory(Long categoryId) {

    }

    // TODO - 각 카테고리 에 해당하는 subcategory 리턴
    @Transactional(readOnly = true)
    public List<SubCategories> getSubCategoriesBelongCategory(Long categoryId) {
        return subCateRepository.findByCategory_Id(categoryId);
    }

}
