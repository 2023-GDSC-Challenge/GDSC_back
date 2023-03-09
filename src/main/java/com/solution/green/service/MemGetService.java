package com.solution.green.service;

import com.solution.green.dto.MemGetDto;
import com.solution.green.entity.Badge;
import com.solution.green.entity.MemberGet;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.BadgeRepository;
import com.solution.green.repository.MemCateRepository;
import com.solution.green.repository.MemberGetRepository;
import com.solution.green.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
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
    private final MemCateRepository memCateRepository;

    @Transactional
    public MemGetDto.Title createTitle(Long memberId, Long titleId) {
        return MemGetDto.Title.fromEntity(
                memberGetRepository.save(MemberGet.builder()
                        .member(memberRepository.findById(memberId)
                                .orElseThrow(() -> new GreenException(NO_MEMBER)))
                        .badge(getBadgeEntity(titleId))
                        .choice(2)
                        .build())
        );
    }

    @Transactional(readOnly = true)
    private Badge getBadgeEntity(Long titleId) {
        return badgeRepository.findById(titleId)
                .orElseThrow(() -> new GreenException(NO_BADGE));
    }

    @Transactional
    public MemGetDto.Title updateTitle(Long memberId, Long titleId) {
        MemberGet memberGet =
                memberGetRepository.findByMember_IdAndChoice(memberId, 2)
                        .orElseThrow(() -> new GreenException(NO_BADGE));
        memberGet.setBadge(getBadgeEntity(titleId));
        return MemGetDto.Title.fromEntity(memberGetRepository.save(memberGet));
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
    @Transactional
    public void createMainBadge(Long memberGetId) {
        MemberGet entity = memberGetRepository.findById(memberGetId)
                .orElseThrow(() -> new GreenException(NO_BADGE));
        entity.setChoice(1);
        memberGetRepository.save(entity);
    }

    @Transactional
    public void updateMainBadge(Long memberId, Long memberGetId) {
        // 이전에 mainBadge 였던 것을 1 -> 0으로 변경
        MemberGet prevMainBadge =
                memberGetRepository.findByMember_IdAndChoice(memberId, 1)
                        .orElseThrow(() -> new GreenException(NO_BADGE));
        prevMainBadge.setChoice(0);
        memberGetRepository.save(prevMainBadge);
        // 새로운 mainBadge 설정 0 -> 1
        createMainBadge(memberGetId);
    }
}
