package com.solution.green.controller;

import com.solution.green.dto.MemberDto;
import com.solution.green.dto.QuestDto;
import com.solution.green.service.QuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class QuestController {
    private final QuestService questService;

    // TODO - 겟리스트 (디폴트로 넘기기)
    // 디폴트 정렬 순서 ... -> 참여자 수(인기도) & 사용자 관심 순서

    // TODO - 마이퀘스트로 추가하기
    // TODO - 멤버가 퀘스트를 하겠다고 등록해서 memberDo db 에 들어갈 때
    /*            Calendar cal = Calendar.getInstance();
            cal.setTime(memberDo.getStartDate());
            cal.add(Calendar.DATE, memberDo.getQuest().getTimeLimit());
            Date dueDate = new Date(cal.getTimeInMillis());
            로 dueDate 설정할 것*/


    @PostMapping("/create-quest") // only for back-end
    public QuestDto.Detail createQuest(
            @Valid @RequestBody QuestDto.Request request) {
        return questService.createQuest(request);
    }
}
