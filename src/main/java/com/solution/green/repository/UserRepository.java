package com.solution.green.repository;

import com.solution.green.entity.User;
//import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  { // extends JpaRepository<User, Long>
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String Email);
    Optional<User> findById(Long no);
    void deleteById(Long id);

}