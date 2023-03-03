package com.solution.green.service;

import com.solution.green.dto.MemDoDto;
import com.solution.green.entity.MemberDo;
import com.solution.green.entity.Quest;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.MemDoRepository;
import com.solution.green.repository.MemberRepository;
import com.solution.green.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.solution.green.code.GreenErrorCode.NO_MEMBER;
import static com.solution.green.code.GreenErrorCode.NO_QUEST;

@Service
@RequiredArgsConstructor
public class MemDoService {
    private final MemDoRepository memDoRepository;
    private final QuestRepository questRepository;
    private final MemberRepository memberRepository;

    public List<MemDoDto.My> getMyQuestNotYetList(Long memberId) {
        return getMyQuestList(memberId, 0);
    }
    public List<MemDoDto.My> getMyQuestIngList(Long memberId) {
        return getMyQuestList(memberId, 1);
    }
    public List<MemDoDto.My> getMyQuestDoneList(Long memberId) {
        return getMyQuestList(memberId, 2);
    }
    @Transactional(readOnly = true)
    public List<MemDoDto.My> getMyQuestList(Long memberId, int stance) {
        return memDoRepository.findByMember_IdAndStanceOrderByDueDateAsc(memberId, stance);
    }
    @Transactional
    public MemDoDto.My addToMyQuest(Long memberId, Long questId) {
        Date now = new Date();
        return MemDoDto.My.fromEntity(
                memDoRepository.save(
                        MemberDo.builder()
                                .quest(getQuestEntity(questId))
                                .member(memberRepository.findById(memberId)
                                        .orElseThrow(() -> new GreenException(NO_MEMBER)))
                                .startDate(now)
                                .stance(1)
                                .dueDate(setDueDate(now, questId))
                                .build()
                )
        );
    }

    private Date setDueDate(Date now, Long questId) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, getQuestEntity(questId).getTimeLimit());
        return new Date(cal.getTimeInMillis());
    }
    @Transactional(readOnly = true)
    private Quest getQuestEntity(Long questId) {
        return questRepository.findById(questId)
                .orElseThrow(() -> new GreenException(NO_QUEST));
    }
}