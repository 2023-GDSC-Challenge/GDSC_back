package com.solution.green.controller;

import com.solution.green.dto.CategoryDto;
import com.solution.green.dto.MemDoDto;
import com.solution.green.service.MemCateService;
import com.solution.green.service.MemDoService;
import com.solution.green.service.QuestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class CategoryController {
    private final MemCateService memCateService;
    private final MemDoService memDoService;
    private final QuestService questService;

    @GetMapping("/getJustOneQuestToMain/{memberId}")
    public MemDoDto.ListView getJustOneQuestToMain(@PathVariable final Long memberId) {
        return memDoService.getJustOneQuestToMain(memberId);
    }

    @GetMapping("/getMemberCategory/{memberId}")
    public List<CategoryDto.Home> getMemberCategory(@PathVariable final Long memberId) {
        return memCateService.getMemberCategoryHome(memberId);
    }
}
