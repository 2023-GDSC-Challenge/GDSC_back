package com.solution.green.repository;

import com.solution.green.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestRepository extends JpaRepository<Quest, Long> {
    long countBySubCategory_Category_Id(Long id);

}
