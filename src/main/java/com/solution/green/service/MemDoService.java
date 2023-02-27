package com.solution.green.service;

import com.solution.green.dto.MemDoDto;
import com.solution.green.repository.MemDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemDoService {
    private final MemDoRepository memDoRepository;

    public List<MemDoDto.My> getMyQuestNotYetList(Long memberId) {
        return getMyQuestList(memberId, 0);
    }
    public List<MemDoDto.My> getMyQuestIngList(Long memberId) {
        return getMyQuestList(memberId, 1);
    }
    public List<MemDoDto.My> getMyQuestDoneList(Long memberId) {
        return getMyQuestList(memberId, 2);
    }
    @Transactional(readOnly = true)
    public List<MemDoDto.My> getMyQuestList(Long memberId, int stance) {
        return memDoRepository.findByMember_IdAndStance(memberId, stance);
    }
}
