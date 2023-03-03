package com.solution.green.controller;

import com.solution.green.dto.MemDoDto;
import com.solution.green.dto.QuestDto;
import com.solution.green.service.MemDoService;
import com.solution.green.service.QuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping("/getQuestNotMyQuestList/{memberId}")
    public List<QuestDto.ListView> getQuestNotMyQuestList(@PathVariable final Long memberId) {
        return questService.getQuestNotMyQuestList(memberId);
    }

    @PostMapping("/add-to-my-quest/{memberId}/{questId}")
    public MemDoDto.My addToMyQuest(@PathVariable final Long memberId,
                                    @PathVariable final Long questId) {
        return memDoService.addToMyQuest(memberId, questId);
    }

    @PostMapping("/create-quest") // only for back-end
    public QuestDto.ListView createQuest(
            @Valid @RequestBody QuestDto.Request request) {
        return questService.createQuest(request);
    }

    @GetMapping("/getQuestDetailView/{questId}")
    public QuestDto.DetailView getQuestDetailView(@PathVariable final Long questId) {
        return questService.getQuestDetailView(questId);
    }
}
