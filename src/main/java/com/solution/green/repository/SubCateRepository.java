package com.solution.green.repository;

import com.solution.green.entity.SubCategories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCateRepository extends JpaRepository<SubCategories, Long> {
    List<SubCategories> findByCategory_Id(Long id);

    SubCategories findByName(String name);
}
