package com.solution.green.service;

import com.solution.green.dto.QuestDto;
import com.solution.green.entity.Quest;
import com.solution.green.entity.SubCategories;
import com.solution.green.repository.QuestRepository;
import com.solution.green.repository.SubCateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class QuestService {
    private final SubCateRepository subCateRepository;
    private final QuestRepository questRepository;
    @Transactional(readOnly = true) // 사용 보류
    public List<SubCategories> getSubCategoriesBelongCategory(Long categoryId) {
        return subCateRepository.findByCategory_Id(categoryId);
    }
    @Transactional
    public QuestDto.Detail createQuest(QuestDto.Request request) {
        return QuestDto.Detail.fromEntity(
                questRepository.save(
                        Quest.builder()
                                .subCategory(
                                        subCateRepository.findByName(
                                                request.getSubCateName()))
                                .name(request.getQuestName())
                                .reward(request.getReward())
                                .memo(request.getMemo())
                                .timeLimit(request.getTimeLimit())
                                .build()
                )
        );
    }
}
