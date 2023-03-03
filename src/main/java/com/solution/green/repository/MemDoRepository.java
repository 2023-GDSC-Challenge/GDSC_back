package com.solution.green.repository;

import com.solution.green.dto.MemDoDto;
import com.solution.green.dto.QuestDto;
import com.solution.green.entity.Category;
import com.solution.green.entity.MemberDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemDoRepository extends JpaRepository<MemberDo, Long> {
    long countByQuest_Id(Long id);
    long countByQuest_SubCategory_CategoryAndStance(Category category, int stance);

    List<MemDoDto.My> findByMember_IdAndStance(Long id, int stance);

    List<QuestDto.Detail> findByMember_Id(Long id);

    List<MemDoDto.My> findByMember_IdAndStanceOrderByDueDateAsc(Long id, int stance);

}