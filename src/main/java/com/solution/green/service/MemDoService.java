package com.solution.green.service;

import com.solution.green.dto.MemDoDto;
import com.solution.green.entity.MemberDo;
import com.solution.green.entity.Quest;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.CertificateImageRepository;
import com.solution.green.repository.MemDoRepository;
import com.solution.green.repository.MemberRepository;
import com.solution.green.repository.QuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.solution.green.code.GreenErrorCode.NO_MEMBER;
import static com.solution.green.code.GreenErrorCode.NO_QUEST;

@Service
@RequiredArgsConstructor
public class MemDoService {
    private final MemDoRepository memDoRepository;
    private final QuestRepository questRepository;
    private final MemberRepository memberRepository;
    private final CertificateImageRepository certificateImageRepository;

    @Transactional
    public MemDoDto.My addToMyQuest(Long memberId, Long questId) {
        Date now = new Date();
        updateQuestChallenger(questId, 1);
        return MemDoDto.My.fromEntity(memDoRepository.save(
                MemberDo.builder()
                        .quest(getQuestEntity(questId))
                        .member(memberRepository.findById(memberId)
                                .orElseThrow(() -> new GreenException(NO_MEMBER)))
                        .startDate(now)
                        .stance(0)
                        .dueDate(setDueDate(now, questId))
                        .build()
                )
        );
    }

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
        return memDoRepository
                .findByMember_IdAndStanceOrderByDueDateAsc(memberId, stance)
                .stream()
                .map(MemDoDto.My::fromEntity)
                .collect(Collectors.toList());
    }

    private Date setDueDate(Date now, Long questId) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(now);
        cal.add(Calendar.DATE, getQuestEntity(questId).getTimeLimit());
        return new Date(cal.getTimeInMillis());
    }

    @Transactional
    private void updateQuestChallenger(Long questId, int how) {
        Quest quest = getQuestEntity(questId);
        quest.setChallenger(quest.getChallenger() + how);
        // how == 1: add challenger
        // how == -1: delete challenger
        questRepository.save(quest);
    }

    @Transactional(readOnly = true)
    private Quest getQuestEntity(Long questId) {
        return questRepository.findById(questId)
                .orElseThrow(() -> new GreenException(NO_QUEST));
    }

    public MemDoDto.DetailView getMyQuestDetailView(Long memberDoId) {
        return MemDoDto.DetailView.fromEntity(
                getMemberDoEntity(memberDoId));
    }

    @Transactional(readOnly = true)
    private MemberDo getMemberDoEntity(Long memberDoId) {
        return memDoRepository.findById(memberDoId)
                .orElseThrow(() -> new GreenException(NO_QUEST));
    }

    public void deleteQuest(Long memberDoId) {
        // certificate DB 에 사진 있으면 전부 삭제
        MemberDo memberDo = getMemberDoEntity(memberDoId);
        Long questId = memberDo.getQuest().getId();
        certificateImageRepository.deleteByMemberDo(memberDo);
        // memberDo DB 에서 퀘스트 기록 삭제
        memDoRepository.deleteById(memberDoId);
        // 퀘스트 challengers -= 1
        updateQuestChallenger(questId, -1);
    }
}
