package com.solution.green.repository;

import com.solution.green.entity.MemberGet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberGetRepository extends JpaRepository<MemberGet, Long> {
    MemberGet findByMember_IdAndChoice(Long id, int choice);
}