package com.solution.green.repository;

import com.solution.green.entity.Category;
import com.solution.green.entity.MemberDo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemDoRepository extends JpaRepository<MemberDo, Long> {
    long countByQuest_SubCategory_Category(Category category);
}
