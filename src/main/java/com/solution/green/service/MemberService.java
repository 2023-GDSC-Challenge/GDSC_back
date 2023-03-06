package com.solution.green.service;

import com.solution.green.dto.MemberDto;
import com.solution.green.entity.Member;
import com.solution.green.entity.MemberGet;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.MemberGetRepository;
import com.solution.green.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.solution.green.code.GreenErrorCode.*;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberGetRepository memberGetRepository;

    @Transactional
    public MemberDto.Response createMember(@NonNull MemberDto.Request request) {
        if (!validateIsEmailRegistered(request.getEmail()))
            return MemberDto.Response.fromEntity(
                    memberRepository.save(
                            Member.builder()
                                    .email(request.getEmail())
                                    .nickname(request.getNickname())
                                    .password(request.getPassword())
                                    .build()));
        else throw new GreenException(ALREADY_REGISTERED);
    }

    public MemberDto.Response login(@NonNull MemberDto.Login loginMember) {
        Member entity = getMemberEntityByEmail(loginMember.getEmail());
        if (loginMember.getPassword().equals(entity.getPassword()))
            return getResponseWithTitle(entity);
        else throw new GreenException(WRONG_PASSWORD);
    }

    @Transactional(readOnly = true)
    public boolean validateIsEmailRegistered(@NonNull String email) {
        return memberRepository.existsByEmail(email);
    }

    public String getUserImageURL(Long userId) {
        return getUserEntityById(userId).getImage();
    }

    public MemberDto.Response getMemberDetail(Long memberId) {
        return getResponseWithTitle(getUserEntityById(memberId));
    }

    private MemberDto.Response getResponseWithTitle(Member member) {
        MemberDto.Response dto =
                MemberDto.Response.fromEntity(member);
        setMemberDtoTitle(dto);
        return dto;
    }

    @Transactional(readOnly = true)
    private void setMemberDtoTitle(MemberDto.Response dto) {
        dto.setTitle(memberGetRepository.findByMember_IdAndChoice(
                dto.getMemberId(), 2)
                .getBadge().getName());
    }

    @Transactional(readOnly = true)
    public List<MemberDto.Response> getAllMembers() {
        List<MemberDto.Response> list = memberRepository.findAll()
                .stream()
                .map(MemberDto.Response::fromEntity)
                .collect(Collectors.toList());
        for (MemberDto.Response dto : list)
            setMemberDtoTitle(dto);
        return list;
    }

    @Transactional
    public MemberDto.Response updateMember(Long memberId,
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

        return getResponseWithTitle(memberRepository.save(member));
    }

    @Transactional
    public void updateMemberImage(Long userId, String uuid) {
        Member member = getUserEntityById(userId);
        member.setImage(uuid);
        memberRepository.save(member);
    }

    @Transactional
    public void deleteMember(Long memberId) {
        memberRepository.deleteById(memberId);
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