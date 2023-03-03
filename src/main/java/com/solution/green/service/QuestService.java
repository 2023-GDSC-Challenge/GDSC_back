package com.solution.green.service;

import com.google.common.collect.Lists;
import com.solution.green.dto.QuestDto;
import com.solution.green.entity.MemberCategory;
import com.solution.green.entity.Quest;
import com.solution.green.entity.SubCategories;
import com.solution.green.repository.MemCateRepository;
import com.solution.green.repository.MemDoRepository;
import com.solution.green.repository.QuestRepository;
import com.solution.green.repository.SubCateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class QuestService {
    private final SubCateRepository subCateRepository;
    private final QuestRepository questRepository;
    private final MemDoRepository memDoRepository;
    private final MemCateRepository memCateRepository;

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
                                        subCateRepository.findByCategory_IdAndName(
                                                request.getParentCategoryId(),
                                                request.getSubCateName()))
                                .name(request.getQuestName())
                                .reward(request.getReward())
                                .memo(request.getMemo())
                                .timeLimit(request.getTimeLimit())
                                .build()
                )
        );
    }

    public List<QuestDto.Detail> getQuestNotMyQuestList(Long memberId) {
        // TODO - member 가 myQuest 에 추가 안한 리스트만 뽑기
        List<QuestDto.Detail> list = questRepository.findAll().stream()
                .map(QuestDto.Detail::fromEntity).collect(Collectors.toList());
        list.removeAll(memDoRepository.findByMember_Id(memberId));
        // TODO - 그 리스트를 사용자 수에 따라 sort
        List<QuestDto.Detail> editList1 = setNowChallenger(list);
        Collections.sort(editList1, (d1, d2) -> d2.getNowChallenger() - d1.getNowChallenger());
        // TODO - 정렬된 리스트를 1/5 으로 분할
        List<List<QuestDto.Detail>> editList2 = Lists.partition(editList1, editList1.size() / 5);
        // TODO - 분할된 리스트를 카테고리에 따라 분류
        // TODO - 분류한 카테고리를 우선순위에 맞게 수정
        // TODO - 리스트 리턴
        return setListToPriorityCateOrder(memberId, editList2);
    }

    private List<QuestDto.Detail> setListToPriorityCateOrder(Long memberId, List<List<QuestDto.Detail>> editList2) {
        List<QuestDto.Detail> finalList = new ArrayList<>();
        List<MemberCategory> priority = memCateRepository.findByMember_IdOrderByPriorityAsc(memberId);
        for (List<QuestDto.Detail> tmpList : editList2) {
            List<QuestDto.Detail> first = new ArrayList<>();
            List<QuestDto.Detail> second = new ArrayList<>();
            List<QuestDto.Detail> third = new ArrayList<>();
            List<QuestDto.Detail> fourth = new ArrayList<>();
            for (QuestDto.Detail quest : tmpList)
                if (isPriorityCateEqualsQuestCate(priority, quest, 0))
                    first.add(quest);
                else if (isPriorityCateEqualsQuestCate(priority, quest, 1))
                    second.add(quest);
                else if (isPriorityCateEqualsQuestCate(priority, quest, 2))
                    third.add(quest);
                else if (isPriorityCateEqualsQuestCate(priority, quest, 3))
                    fourth.add(quest);
            finalList.addAll(first);
            finalList.addAll(second);
            finalList.addAll(third);
            finalList.addAll(fourth);
        }
        return finalList;
    }

    private static boolean isPriorityCateEqualsQuestCate(
            List<MemberCategory> priority, QuestDto.Detail quest, int order) {
        return priority.get(order).getCategory().getId()
                .equals(quest.getCategoryDto().getCategoryId());
    }

    private List<QuestDto.Detail> setNowChallenger(List<QuestDto.Detail> list) {
        List<QuestDto.Detail> editList = new ArrayList<>();
        for (QuestDto.Detail detail : list) {
            detail.setNowChallenger((int) memDoRepository.countByQuest_Id(detail.getQuestId()));
            editList.add(detail);
        }
        return editList;
    }
}
