package com.solution.green.service;

import com.solution.green.dto.MemberDto;
import com.solution.green.entity.Member;
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

import java.util.ArrayList;
import java.util.List;

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
        if (!validateIsEmailRegistered(request.getEmail()))
            return getMemberResponse(
                    saveMemberEntity(
                            Member.builder()
                                    .email(request.getEmail())
                                    .nickname(request.getNickname())
                                    .password(request.getPassword())
                                    .build()));
        else throw new GreenException(ALREADY_REGISTERED);
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
        List<Member> entityList = memberRepository.findAll();
        for (Member entity : entityList)
            list.add(getMemberResponse(entity));
        return list;
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
}