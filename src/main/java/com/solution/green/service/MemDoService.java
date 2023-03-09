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
import static com.solution.green.code.GreenErrorCode.NO_MEMBER;
import static com.solution.green.code.GreenErrorCode.NO_QUEST;

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

    private final Double[] achievementList = {
            Double.valueOf(30),
            Double.valueOf(50),
            Double.valueOf(70),
            Double.valueOf(100)};

    @Transactional
    public MemDoDto.ListView addToMyQuest(Long memberId, Long questId) {
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

    public List<MemDoDto.ListView> getMyQuestNotYetList(Long memberId) {
        return getMyQuestList(memberId, 0);
    }

    public List<MemDoDto.ListView> getMyQuestIngList(Long memberId) {
        return getMyQuestList(memberId, 1);
    }

    public List<MemDoDto.ListView> getMyQuestDoneList(Long memberId) {
        return getMyQuestList(memberId, 2);
    }

    @Transactional(readOnly = true)
    public List<MemDoDto.ListView> getMyQuestList(Long memberId, int stance) {
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
        Long cateId = questRepository.findById(memberDo.getQuest().getId())
                .orElseThrow(() -> new GreenException(NO_QUEST))
                .getSubCategory().getCategory().getId();
        // 바꾸기 전의 rate 저장
        Long prevDoneCount = memCateService.getDoneQuestPerCategory(cateId);
        Long count = memCateService.getQuestNumPerCategory(cateId);
        Double prevRate = Double.valueOf(0);
        if (!prevDoneCount.equals(0))
            prevRate = Double.valueOf(prevDoneCount / count);
        System.out.println(prevRate);
        System.out.println();
        // stance 변경 false -> true(done)
        memberDo.setStance(QUEST_DONE.getBool());
        MemberDo tmp = memDoRepository.save(memberDo); // TODO - 얘도 안된다
        System.out.println(memDoRepository.findById(tmp.getId()).get().getStance());
        System.out.println();
        // 바꾸고 나서의 rate 비교 -> 업데이트된 뱃지가 있으면 마이겟 디비에 추가
        Double curRate = Double.valueOf(prevDoneCount + 1 / count);
        System.out.println(curRate);
        for (Double i : achievementList)
            if (prevRate < i && i <= curRate) {
                memberGetRepository.save(MemberGet.builder()
                        .member(member)
                        .badge(badgeRepository
                                .findByCategory_IdAndAchievement(cateId, i))
                        .build());
//                System.out.println();
            }
//        System.out.println();
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
        System.out.println(getCertificateImageCount(memberDoId));
        System.out.println(getQuestIteration(memberDoId));
        System.out.println();
        if (getCertificateImageCount(memberDoId) == getQuestIteration(memberDoId)) {
            System.out.println("!!!!!!!!!!!");
            updateQuestStance(memberDoId);}
    }

    @Transactional
    public void validateFailedQuest() {
        List<MemberDo> questList = memDoRepository.findByDueDateLessThan(new Date());
        for (MemberDo entity: questList)
            if (getCertificateImageCount(entity.getId())
                    < getQuestIteration(entity.getId())) {
                // 퀘스트 challenger -= 1
                updateQuestChallenger(entity.getQuest().getId(), -1);
                // memberDo DB 에서 삭제
                memDoRepository.delete(entity);
            }
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
