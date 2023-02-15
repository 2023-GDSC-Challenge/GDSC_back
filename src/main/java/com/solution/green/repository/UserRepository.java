package com.solution.green.repository;

import com.solution.green.entity.CreateMember;
//import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  { // extends JpaRepository<User, Long>
    boolean existsByEmail(String email);
    Optional<CreateMember> findByEmail(String Email);
    Optional<CreateMember> findById(Long no);
    void deleteById(Long id);

}