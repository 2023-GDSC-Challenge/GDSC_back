package com.solution.green.service;

import com.solution.green.dto.MemberDto;
import com.solution.green.dto.ReportDto;
import com.solution.green.entity.Member;
import com.solution.green.entity.MemberDo;
import com.solution.green.entity.Quest;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.MemDoRepository;
import com.solution.green.repository.MemberGetRepository;
import com.solution.green.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

import static com.solution.green.code.GreenCode.QUEST_DONE;
import static com.solution.green.code.GreenCode.QUEST_ING;
import static com.solution.green.code.GreenErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberGetRepository memberGetRepository;
    private final MemDoRepository memDoRepository;

    public MemberDto.Response createMember(@NonNull MemberDto.Request request) {
        if (!validateIsEmailRegistered(request.getEmail())) {
            Member member = saveMemberEntity(
                    Member.builder()
                            .email(request.getEmail())
                            .nickname(request.getNickname())
                            .password(request.getPassword())
                            .build());
            String randomString = setRandomNumber(member.getId());
            member.setRandomCode(randomString);
            saveMemberEntity(member);
            return getMemberResponse(member);

        } else throw new GreenException(ALREADY_REGISTERED);
    }


    public MemberDto.ToModel login(@NonNull MemberDto.Login loginMember) {
        Member entity = getMemberEntityByEmail(loginMember.getEmail());
        if (loginMember.getPassword().equals(entity.getPassword()))
            return MemberDto.ToModel.fromEntity(entity);
        else throw new GreenException(WRONG_PASSWORD);
    }

    public MemberDto.Response getMemberDetail(Long memberId) {
        return getMemberResponse(getUserEntityById(memberId));
    }

    public void updateMemberImage(Long userId, String uuid) {
        Member member = getUserEntityById(userId);
        member.setImage(uuid);
        saveMemberEntity(member);
    }

    public MemberDto.Response updateMember(
            Long memberId,
            @Nullable MemberDto.Request updateRequest) {
        Member member = getUserEntityById(memberId);

        if (updateRequest.getNickname() != null)
            member.setNickname(updateRequest.getNickname());
        if (updateRequest.getEmail() != null)
            // 변경하는 email 은 기존에 다른 member 가 등록한 적 없는 것이어야 한다.
            if (!validateIsEmailRegistered(updateRequest.getEmail())
                    || updateRequest.getEmail().equals(member.getEmail()))
                member.setEmail(updateRequest.getEmail());
            else throw new GreenException(ALREADY_REGISTERED);
        if (updateRequest.getPassword() != null)
            member.setPassword(updateRequest.getPassword());

        return getMemberResponse(saveMemberEntity(member));
    }

    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
    }

    @Transactional(readOnly = true)
    public boolean validateIsEmailRegistered(@NonNull String email) {
        return memberRepository.existsByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<MemberDto.Response> getAllMembers() {
        List<MemberDto.Response> list = new ArrayList<>();
        List<Member> entityList = memberRepository.findByIdNotNullOrderByRewardDesc();
        for (Member entity : entityList)
            list.add(getMemberResponse(entity));
        return list;
    }

    public String getMemberRandomCode(Long memberId) {
        return getUserEntityById(memberId).getRandomCode();
    }

    private String setRandomNumber(Long id) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 7;
        Random random = new Random();
        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97) && (i <= 100 || i >= 109))
                // id 변환해서 나오는 문자도 filter 로 제외함
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        return setIdToRandomNumber(id) + generatedString;
    }

    private String setIdToRandomNumber(Long id) {
        int[] arrNum = Stream.of(String.valueOf(id).split(""))
                .mapToInt(Integer::parseInt).toArray();
        String result = "";
        for (int num : arrNum) result += String.valueOf((char) (num + 100));
        return result;
    }

    @Transactional(readOnly = true)
    private MemberDto.Response getMemberResponse(Member entity) {
        MemberDto.Response dto = MemberDto.Response.fromEntity(entity);
        if (memberGetRepository.existsByMember_IdAndChoice(dto.getMemberId(), 2))
            dto.setTitle(getBadgeName(dto, 2));
        if (memberGetRepository.existsByMember_IdAndChoice(dto.getMemberId(), 1))
            dto.setMainBadge(getBadgeName(dto, 1));
        dto.setProgressQuests(
                memDoRepository.countByMember_IdAndStance(
                        entity.getId(), QUEST_ING.getBool()));
        dto.setSuccessQuests(
                memDoRepository.countByMember_IdAndStance(
                        entity.getId(), QUEST_DONE.getBool()));
        dto.setBadgeCount(
                memberGetRepository.countByMember_IdAndChoiceNot(
                        entity.getId(), 2));
        return dto;
    }

    @Transactional(readOnly = true)
    private String getBadgeName(MemberDto.Response dto, int choice) {
        return memberGetRepository
                .findByMember_IdAndChoice(dto.getMemberId(), choice)
                .orElseThrow(() -> new GreenException(NO_BADGE))
                .getBadge().getName();
    }

    @Transactional
    private Member saveMemberEntity(@NotNull Member member) {
        return memberRepository.save(member);
    }


    @Transactional(readOnly = true)
    private Member getMemberEntityByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new GreenException(WRONG_EMAIL));
    }

    @Transactional(readOnly = true)
    private Member getUserEntityById(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new GreenException(NO_MEMBER));
    }

    public int getMemberRank(Long memberId) {
        List<MemberDto.Response> list = getAllMembers();
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).getMemberId().equals(memberId))
                return i + 1;
        return -1;
    }

    public ReportDto getWeeklyReport(Long memberId) {
        // 이번 주의 첫 날(일요일), 마지막 날(월요일) 세팅
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        Date startSunday = calendar.getTime();
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        Date endSaturday = calendar.getTime();
        // 레포트 작성
        return getReport(memberId, startSunday, endSaturday);
    }

    public ReportDto getMonthlyReport(Long memberId) {
        // 이번 달의 첫 날, 마지막 날 세팅
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        Date startMonth = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        Date endMonth = calendar.getTime();
        // 레포트 작성
        return getReport(memberId, startMonth, endMonth);
    }

    @NotNull
    @Transactional(readOnly = true)
    private ReportDto getReport(Long memberId, Date firstDate, Date lastDate) {
        // 카테고리 별 이번주 리워드 세팅
        List<MemberDo> doneList =
                memDoRepository.findByMember_IdAndDueDateBetweenAndStance(
                        memberId, firstDate, lastDate, true);
        Double homeRewardSum = 0.0;
        Double consumptionRewardSum = 0.0;
        Double transportRewardSum = 0.0;
        Double foodRewardSum = 0.0;
        for (MemberDo entity : doneList) {
            Quest quest = entity.getQuest();
            Double reward = quest.getReward();
            if (getCateIdFromQuest(quest) == 1) homeRewardSum += reward;
            else if (getCateIdFromQuest(quest) == 2)
                consumptionRewardSum += reward;
            else if (getCateIdFromQuest(quest) == 3) transportRewardSum += reward;
            else if (getCateIdFromQuest(quest) == 4) foodRewardSum += reward;
        }
        return ReportDto.builder()
                .firstDate(firstDate)
                .lastDate(lastDate)
                .homeRewardSum(homeRewardSum)
                .consumptionRewardSum(consumptionRewardSum)
                .transportRewardSum(transportRewardSum)
                .foodRewardSum(foodRewardSum)
                // 퀘스트 달성도 세팅
                .totalQuestCount(
                        (int) memDoRepository.countByMember_IdAndDueDateBetween(
                                memberId, firstDate, lastDate))
                .doneQuestCount(doneList.size())
                .build();

    }

    private static Long getCateIdFromQuest(Quest quest) {
        return quest.getSubCategory().getCategory().getId();
    }
}