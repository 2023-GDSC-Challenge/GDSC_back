package com.solution.green.controller;

import com.solution.green.dto.QuestDto;
import com.solution.green.service.MemDoService;
import com.solution.green.service.QuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class QuestController {
    private final QuestService questService;
    private final MemDoService memDoService;

    @PostMapping("/add-to-my-quest/{memberId}/{questId}")
    public void addToMyQuest(@PathVariable final Long memberId,
                             @PathVariable final Long questId) {
        memDoService.addToMyQuest(memberId, questId);
        // TODO - 있는데 또 추가하려고 할 경우 에러처리
    }

    @PostMapping("/create-quest") // only for back-end
    public QuestDto.ListView createQuest(@Valid @RequestBody QuestDto.Request request) {
        return questService.createQuest(request);
    }

    @GetMapping("/getQuestNotMyQuestList/{memberId}")
    public List<QuestDto.ListView> getQuestNotMyQuestList(
            @PathVariable final Long memberId) {
        return questService.getQuestNotMyQuestList(memberId);
    }

    @GetMapping("/getQuestDetailView/{questId}")
    public QuestDto.DetailView getQuestDetailView(@PathVariable final Long questId) {
        return questService.getQuestDetailView(questId);
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul")
    // every midnight // TODO - testing 미루기 - 테스트케이스가 좀 더 쌓이고!
    public void validateFailedQuest() {
        memDoService.validateFailedQuest();
    }
}
