package com.solution.green.repository;

import com.solution.green.dto.MemberDto;
import com.solution.green.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);
    Optional<Member> findByEmail(String Email);
    void deleteById(Long id);

}