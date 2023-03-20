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

    @Scheduled(cron = "0 0 0 * * *", zone = "Asia/Seoul") // every midnight
    public void autoValidateFailedQuest() {
        memDoService.validateFailedQuest();
    }

    @GetMapping("/forceValidateFailedQuest")
    public void forceValidateFailedQuest() {
        memDoService.validateFailedQuest();
    }
}
