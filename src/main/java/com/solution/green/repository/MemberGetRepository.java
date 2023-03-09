package com.solution.green.repository;

import com.solution.green.entity.MemberGet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberGetRepository extends JpaRepository<MemberGet, Long> {
    MemberGet findByMember_IdAndChoice(Long id, int choice);

    List<MemberGet> findByMember_IdAndChoiceNotOrderByBadge_AchievementDesc(Long id, int choice);

    @Override
    Optional<MemberGet> findById(Long aLong);

    long countByMember_IdAndChoiceNot(Long id, int choice);
}