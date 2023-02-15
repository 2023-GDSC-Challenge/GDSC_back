package com.solution.green.repository;

import com.solution.green.dto.MemberDto;
//import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  { // extends JpaRepository<User, Long>
    boolean existsByEmail(String email);
    Optional<MemberDto> findByEmail(String Email);
    Optional<MemberDto> findById(Long no);
    void deleteById(Long id);

}