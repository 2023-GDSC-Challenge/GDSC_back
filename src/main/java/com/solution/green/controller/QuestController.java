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

    @PostMapping("/create-quest") // only for back-end
    public QuestDto.ListView createQuest(
            @Valid @RequestBody QuestDto.Request request) {
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

    @PostMapping("/add-to-my-quest/{memberId}/{questId}")
    public void addToMyQuest(@PathVariable final Long memberId,
                                    @PathVariable final Long questId) {
        memDoService.addToMyQuest(memberId, questId); // void로 변경
    }

    /* TODO - 퀘스트 완료 처리 고민해야 함!
     *   1. 실패 -> questController 에 만들자
     *       1) 자정마다 프론트에서 전체 함수 호출
     *           - 사진 개수 안 맞는 애들 포기(?)로 변경할 것
     *               - 그냥 myquest 에서 삭제할 것
     *               - 근데 또 그냥 삭제하면 안됨 -> 삭제한 리스트 전송해줘야하는거아냐?
     *           - 해당 퀘스트 challenger -= 1 */
    @PatchMapping("/uploadCertificateImage/{memberDoId}")
    public void uploadCertificateImage() {
        // TODO - 자정마다 호출해야한다 ~
        // TODO - due < 현재 인 애들 중
            // TODO - 사진 개수 < iter 이면
                // TODO - 탈락된 애들 list 에 추가해야함
                    // TODO - 그 퀘스트 challenger -=1
    }
}
