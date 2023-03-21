package com.solution.green.service;

import com.solution.green.dto.MemDoDto;
import com.solution.green.entity.Member;
import com.solution.green.entity.MemberDo;
import com.solution.green.entity.MemberGet;
import com.solution.green.entity.Quest;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.solution.green.code.GreenCode.QUEST_DONE;
import static com.solution.green.code.GreenCode.QUEST_ING;
import static com.solution.green.code.GreenErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemDoService {
    private final MemCateService memCateService;
    private final MemDoRepository memDoRepository;
    private final QuestRepository questRepository;
    private final MemberRepository memberRepository;
    private final CertificateImageRepository certificateImageRepository;
    private final MemberGetRepository memberGetRepository;
    private final BadgeRepository badgeRepository;
    private final QuestService questService;
    private final CertificateService certificateService;

    private final Double[] achievementList = {
            Double.valueOf(30),
            Double.valueOf(50),
            Double.valueOf(70),
            Double.valueOf(100)};

    @Transactional
    public MemDoDto.ListView addToMyQuest(Long memberId, Long questId) {
        if (memDoRepository.existsByMember_IdAndQuest_Id(memberId, questId))
            throw new GreenException(ALREADY_ADDED);

        Date now = new Date();
        updateQuestChallenger(questId, 1);
        return MemDoDto.ListView.fromEntity(memDoRepository.save(
                        MemberDo.builder()
                                .quest(getQuestEntity(questId))
                                .member(memberRepository.findById(memberId)
                                        .orElseThrow(() -> new GreenException(NO_MEMBER)))
                                .startDate(now)
                                .stance(QUEST_ING.getBool())
                                .dueDate(setDueDate(now, questId))
                                .build()
                )
        );
    }

    public List<MemDoDto.ListView> getMyQuestIngList(Long memberId) {
        return getMyQuestList(memberId, QUEST_ING.getBool());
    }

    public List<MemDoDto.ListView> getMyQuestDoneList(Long memberId) {
        return getMyQuestList(memberId, QUEST_DONE.getBool());
    }

    @Transactional(readOnly = true)
    public List<MemDoDto.ListView> getMyQuestList(Long memberId, boolean stance) {
        return memDoRepository
                .findByMember_IdAndStanceOrderByDueDateAsc(memberId, stance)
                .stream()
                .map(MemDoDto.ListView::fromEntity)
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

    @Transactional
    public void updateQuestStance(Long memberDoId) {
        // setting
        MemberDo memberDo = getMemberDoEntity(memberDoId);
        Member member = memberDo.getMember();
        Quest quest = questRepository.findById(memberDo.getQuest().getId())
                .orElseThrow(() -> new GreenException(NO_QUEST));
        Long cateId = quest.getSubCategory().getCategory().getId();

        // stance 변경 false -> true(done)
        memberDo.setStance(QUEST_DONE.getBool());
        // end date 를 현재 시각으로 설정
        memberDo.setEndDate(new Date());

        // 퀘스트 완료 시 멤버 reward up
        member.setReward(member.getReward() + quest.getReward());
        memberRepository.save(member);

        // 진행률 세팅
        Long doneCount =
                memCateService.getDoneQuestPerCategory(member.getId(), cateId);
        Long count = memCateService.getQuestNumPerCategory(cateId);
        Double curRate = (double) doneCount / count * 100;
        Double prevRate = (double) (doneCount - 1) / count * 100;

        // 받을 뱃지 있나 확인
        for (Double i : achievementList)
            if (prevRate < i && i <= curRate)
                memberGetRepository.save(MemberGet.builder()
                        .member(member)
                        .badge(badgeRepository
                                .findByCategory_IdAndAchievement(cateId, i))
                        .build());
    }

    @Transactional(readOnly = true)
    private Quest getQuestEntity(Long questId) {
        return questRepository.findById(questId)
                .orElseThrow(() -> new GreenException(NO_QUEST));
    }

    public MemDoDto.DetailView getMyQuestDetailView(Long memberDoId) {
        MemDoDto.DetailView dto =
                MemDoDto.DetailView.fromEntity(getMemberDoEntity(memberDoId));
        if (dto.getStance())
            dto.setCertificateImages(certificateService.getCertificateImages(memberDoId));
        return dto;
        // TODO - 이미지 추가 완료 -> 테스트 후 배포할 것
        // TODO - stance 완료됐을 때만으로 처리할 것
    }

    @Transactional
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

    @Transactional(readOnly = true)
    public MemDoDto.ListView getJustOneQuestToMain(Long memberId) {
        // 진행중인 퀘스트가 있으면 -> 그 중 가장 우선순위 높은거
        if (memDoRepository.existsByMember_IdAndStance(memberId, QUEST_ING.getBool()))
            return MemDoDto.ListView.fromEntity(
                    memDoRepository.findFirstByMember_IdAndStanceOrderByDueDateAsc(
                            memberId, QUEST_ING.getBool())
            );
            // 없으면 -> 퀘스트리스트 중 가장 우선순위 높은거
        else return MemDoDto.ListView.builder()
                .memDoId(Long.valueOf(-1)) // DB 에 저장된 값이 X -> 임의로 설정
                .questDto(questService.getQuestNotMyQuestList(memberId).get(0))
                .build();
    }

    public void validateQuestIsDone(Long memberDoId) {
        if (getCertificateImageCount(memberDoId) == getQuestIteration(memberDoId))
            updateQuestStance(memberDoId);
    }

    @Transactional
    public void validateFailedQuest() {
        List<MemberDo> questList = memDoRepository.findByDueDateLessThan(new Date());
        for (MemberDo entity : questList)
            if (getCertificateImageCount(entity.getId())
                    < getQuestIteration(entity.getId()))
                deleteQuest(entity.getId());
    }

    @Transactional(readOnly = true)
    private long getCertificateImageCount(Long memberDoId) {
        return certificateImageRepository.countByMemberDo_Id(memberDoId);
    }

    private Integer getQuestIteration(Long memberDoId) {
        return getMemberDoEntity(memberDoId).getQuest().getIteration();
    }

    @Transactional(readOnly = true)
    private MemberDo getMemberDoEntity(Long memberDoId) {
        return memDoRepository.findById(memberDoId)
                .orElseThrow(() -> new GreenException(NO_QUEST));
    }
}
