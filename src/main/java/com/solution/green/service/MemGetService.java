package com.solution.green.service;

import com.solution.green.dto.MemGetDto;
import com.solution.green.entity.Badge;
import com.solution.green.entity.MemberGet;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.BadgeRepository;
import com.solution.green.repository.MemberGetRepository;
import com.solution.green.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.solution.green.code.GreenErrorCode.NO_BADGE;
import static com.solution.green.code.GreenErrorCode.NO_MEMBER;

@Service
@RequiredArgsConstructor
public class MemGetService {
    private final MemberGetRepository memberGetRepository;
    private final MemberRepository memberRepository;
    private final BadgeRepository badgeRepository;

    @Transactional
    public MemGetDto.Title createTitle(Long memberId, Long titleId) {
        return MemGetDto.Title.fromEntity(
                saveMemberGetEntity(MemberGet.builder()
                        .member(memberRepository.findById(memberId)
                                .orElseThrow(() -> new GreenException(NO_MEMBER)))
                        .badge(getBadgeEntity(titleId))
                        .choice(2)
                        .build())
        );
    }

    @Transactional
    public void createMainBadge(Long memberGetId) {
        MemberGet entity = memberGetRepository.findById(memberGetId)
                .orElseThrow(() -> new GreenException(NO_BADGE));
        entity.setChoice(1);
        memberGetRepository.save(entity);
    }

    @Transactional(readOnly = true)
    public List<MemGetDto.List> getMyBadge(Long memberId) {
        return memberGetRepository
                .findByMember_IdAndChoiceNotOrderByBadge_AchievementDesc(
                        memberId, 2)
                .stream()
                .map(MemGetDto.List::fromEntity)
                .collect(Collectors.toList());
    }

    public void updateMainBadge(Long memberId, Long memberGetId) {
        // 이전에 mainBadge 였던 것을 1 -> 0으로 변경
        MemberGet prevMainBadge = getMemGetByMemIdAndChoice(memberId, 1);
        prevMainBadge.setChoice(0);
        saveMemberGetEntity(prevMainBadge);
        // 새로운 mainBadge 설정 0 -> 1
        createMainBadge(memberGetId);
    }

    public MemGetDto.Title updateTitle(Long memberId, Long titleId) {
        MemberGet memberGet = getMemGetByMemIdAndChoice(memberId, 2);
        memberGet.setBadge(getBadgeEntity(titleId));
        return MemGetDto.Title.fromEntity(saveMemberGetEntity(memberGet));
    }

    @NotNull
    @Transactional
    private MemberGet saveMemberGetEntity(MemberGet memberGet) {
        return memberGetRepository.save(memberGet);
    }

    @Transactional(readOnly = true)
    private Badge getBadgeEntity(Long titleId) {
        return badgeRepository.findById(titleId)
                .orElseThrow(() -> new GreenException(NO_BADGE));
    }

    @Transactional(readOnly = true)
    private MemberGet getMemGetByMemIdAndChoice(Long memberId, int choice) {
        return memberGetRepository.findByMember_IdAndChoice(memberId, choice)
                .orElseThrow(() -> new GreenException(NO_BADGE));
    }
}
