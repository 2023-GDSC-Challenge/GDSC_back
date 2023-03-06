package com.solution.green.service;

import com.solution.green.code.GreenErrorCode;
import com.solution.green.dto.MemGetDto;
import com.solution.green.entity.Badge;
import com.solution.green.entity.MemberGet;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.BadgeRepository;
import com.solution.green.repository.MemberGetRepository;
import com.solution.green.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
                memberGetRepository.findByMember_IdAndChoice(memberId, 2);
        memberGet.setBadge(getBadgeEntity(titleId));
        return MemGetDto.Title.fromEntity(memberGetRepository.save(memberGet));
    }
}
