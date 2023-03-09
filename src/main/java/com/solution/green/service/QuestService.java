package com.solution.green.service;

import com.google.common.collect.Lists;
import com.solution.green.dto.QuestDto;
import com.solution.green.entity.MemberCategory;
import com.solution.green.entity.MemberDo;
import com.solution.green.entity.Quest;
import com.solution.green.entity.SubCategories;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.MemCateRepository;
import com.solution.green.repository.MemDoRepository;
import com.solution.green.repository.QuestRepository;
import com.solution.green.repository.SubCateRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static com.solution.green.code.GreenErrorCode.NO_QUEST;


@Service
@RequiredArgsConstructor
public class QuestService {
    private final SubCateRepository subCateRepository;
    private final QuestRepository questRepository;
    private final MemDoRepository memDoRepository;
    private final MemCateRepository memCateRepository;

    public List<QuestDto.ListView> getQuestNotMyQuestList(Long memberId) {
        // member 가 myQuest 에 추가 안한 리스트만 뽑기
        List<QuestDto.ListView> list = getQuestList(memberId);
        // 그 리스트를 사용자 수에 따라 sort
        List<QuestDto.ListView> editList1 = setNowChallenger(list);
        Collections.sort(editList1, (d1, d2) -> d2.getChallenger() - d1.getChallenger());
        // 정렬된 리스트를 1/3 으로 분할
        List<List<QuestDto.ListView>> editList2 = Lists.partition(editList1, editList1.size() / 3);
        // 분할한 리스트 1) 카테고리에 따라 분류 2) 우선순위에 따라 정렬
        return setListToPriorityCateOrder(memberId, editList2);
    } // TODO - testing

    @Transactional
    public QuestDto.ListView createQuest(QuestDto.Request request) {
        return QuestDto.ListView.fromEntity(questRepository.save(
                        Quest.builder()
                                .subCategory(
                                        subCateRepository.findByCategory_IdAndName(
                                                request.getParentCategoryId(),
                                                request.getSubCateName()))
                                .name(request.getQuestName())
                                .reward(request.getReward())
                                .briefing(request.getMemo())
                                .timeLimit(request.getTimeLimit())
                                .build()
                )
        );
    }

    @Transactional(readOnly = true)
    public QuestDto.DetailView getQuestDetailView(Long questId) {
        return QuestDto.DetailView.fromEntity(questRepository.findById(questId)
                .orElseThrow(() -> new GreenException(NO_QUEST)));
    }

    @Transactional(readOnly = true) // 사용 보류
    public List<SubCategories> getSubCategoriesBelongCategory(Long categoryId) {
        return subCateRepository.findByCategory_Id(categoryId);
    }


    private List<QuestDto.ListView> getQuestList(Long memberId) {
        List<QuestDto.ListView> list = getAllQuestList();
        List<Long> questIdList = getQuestIdListFromMemDoDto(memberId);
        List<QuestDto.ListView> finalList = new ArrayList<>();
        for (QuestDto.ListView quest : list)
            if (!questIdList.contains(quest.getQuestId())) finalList.add(quest);
        return finalList;
    }

    private static boolean isPriorityCateEqualsQuestCate(
            List<MemberCategory> priority, QuestDto.ListView quest, int order) {
        return priority.get(order).getCategory().getId()
                .equals(quest.getCategoryDto().getCategoryId());
    }

    @NotNull
    @Transactional(readOnly = true)
    private List<QuestDto.ListView> getAllQuestList() {
        return questRepository.findAll().stream()
                .map(QuestDto.ListView::fromEntity).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    private List<Long> getQuestIdListFromMemDoDto(Long memberId) {
        List<MemberDo> list = memDoRepository.findByMember_Id(memberId);
        List<Long> questIdList = new ArrayList<>();
        for (MemberDo memberDo : list)
            questIdList.add(memberDo.getQuest().getId());
        return questIdList;
    }

    @Transactional(readOnly = true)
    private List<QuestDto.ListView> setListToPriorityCateOrder(Long memberId, List<List<QuestDto.ListView>> editList2) {
        List<QuestDto.ListView> finalList = new ArrayList<>();
        List<MemberCategory> priority =
                memCateRepository.findByMember_IdOrderByPriorityAsc(memberId);
        for (List<QuestDto.ListView> tmpList : editList2) {
            // 분할된 리스트를 카테고리에 따라 분류
            List<QuestDto.ListView> first = new ArrayList<>();
            List<QuestDto.ListView> second = new ArrayList<>();
            List<QuestDto.ListView> third = new ArrayList<>();
            List<QuestDto.ListView> fourth = new ArrayList<>();
            for (QuestDto.ListView quest : tmpList)
                if (isPriorityCateEqualsQuestCate(priority, quest, 0))
                    first.add(quest);
                else if (isPriorityCateEqualsQuestCate(priority, quest, 1))
                    second.add(quest);
                else if (isPriorityCateEqualsQuestCate(priority, quest, 2))
                    third.add(quest);
                else if (isPriorityCateEqualsQuestCate(priority, quest, 3))
                    fourth.add(quest);
            // 분류한 카테고리를 우선순위에 맞게 수정
            finalList.addAll(first);
            finalList.addAll(second);
            finalList.addAll(third);
            finalList.addAll(fourth);
        }
        return finalList; // 리스트 리턴
    }

    @Transactional(readOnly = true)
    private List<QuestDto.ListView> setNowChallenger(List<QuestDto.ListView> list) {
        List<QuestDto.ListView> editList = new ArrayList<>();
        for (QuestDto.ListView listView : list) {
            listView.setChallenger(
                    (int) memDoRepository.countByQuest_Id(listView.getQuestId())
            );
            editList.add(listView);
        }
        return editList;
    }
}
