package com.solution.green.controller;

import com.solution.green.dto.MemDoDto;
import com.solution.green.service.MemDoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class MyQuestController {
    private final MemDoService memDoService;

    @GetMapping("/getMyQuestNotYetList/{memberId}")
    public List<MemDoDto.My> getMyQuestNotYetList(@PathVariable final Long memberId) {
        return memDoService.getMyQuestNotYetList(memberId);
    }
    @GetMapping("/getMyQuestIngList/{memberId}")
    public List<MemDoDto.My> getMyQuestIngList(@PathVariable final Long memberId) {
        return memDoService.getMyQuestIngList(memberId);
    }
    @GetMapping("/getMyQuestDoneList/{memberId}")
    public List<MemDoDto.My> getMyQuestDoneList(@PathVariable final Long memberId) {
        return memDoService.getMyQuestDoneList(memberId);
    }

    @GetMapping("/getMyQuestDetailView/{memberDoId}")
    public MemDoDto.DetailView getMyQuestDetailView(
            @PathVariable final Long memberDoId) {
        return memDoService.getMyQuestDetailView(memberDoId);
    }

    // 인증페이지 - 사진 업로드 필요
    //
    //+) 인증한 사진 모아둔 갤러리 보여줘야함
}