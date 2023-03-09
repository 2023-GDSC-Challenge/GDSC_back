package com.solution.green.repository;

import com.solution.green.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long> {
    Badge findByCategory_IdAndAchievement(Long id, Double achievement);
}