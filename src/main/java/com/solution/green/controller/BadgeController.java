package com.solution.green.controller;

import com.solution.green.dto.MemDoDto;
import com.solution.green.dto.MemGetDto;
import com.solution.green.service.MemGetService;
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
public class BadgeController {
    private final MemGetService memGetService;

    @GetMapping("/getMyBadge/{memberId}")
    public List<MemGetDto.List> getMyBadge(@PathVariable final Long memberId) {
        return memGetService.getMyBadge(memberId);
    }

    // TODO - 뱃지 고르는 메소드
    @PostMapping("/updateMainBadge")
    public void updateMainBadge(@Valid @RequestBody Long memberGetId) {
        memGetService.updateMainBadge(memberGetId);
    }
    // TODO - 뱃지 변경하는 메소드
}
