package com.solution.green.repository;

import com.solution.green.dto.MemDoDto;
import com.solution.green.dto.QuestDto;
import com.solution.green.entity.Category;
import com.solution.green.entity.MemberDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MemDoRepository extends JpaRepository<MemberDo, Long> {
    boolean existsByMember_IdAndQuest_Id(Long memberId, Long questId);
    List<MemberDo> findByDueDateLessThan(Date dueDate);
    long countByQuest_Id(Long id);
    long countByQuest_SubCategory_CategoryAndStance(Category category, Boolean stance);

    long countByMember_IdAndQuest_SubCategory_CategoryAndStance(Long id, Category category, Boolean stance);

    long countByMember_IdAndStance(Long id, Boolean stance);

    List<MemberDo> findByMember_Id(Long id);

    boolean existsByMember_IdAndStance(Long id, Boolean stance);

    List<MemberDo> findByMember_IdAndStanceOrderByDueDateAsc(Long id, Boolean stance);

    MemberDo findFirstByMember_IdAndStanceOrderByDueDateAsc(Long id, Boolean stance);

    @Override
    Optional<MemberDo> findById(Long aLong);
}
