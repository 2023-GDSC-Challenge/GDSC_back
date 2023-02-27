package com.solution.green.repository;

import com.solution.green.entity.MemberCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemCateRepository extends JpaRepository<MemberCategory, Long> {
    List<MemberCategory> findByMember_IdOrderByPriorityAsc(Long id);
}
