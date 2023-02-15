package com.solution.green.controller;

import com.solution.green.dto.MemberDto;
import com.solution.green.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j // log
@CrossOrigin(origins = "*")
public class MemberController {
    @Autowired
    MemberService memberService;

    @PostMapping("/create-member")
    public String createMember(@Valid @RequestBody MemberDto.Request request)
            throws Exception {
        log.info("[request]: {}", request);
        return memberService.createMember(request);
    }

    @GetMapping("/members")
    public List<MemberDto.Response> getAllMembers() throws Exception{
        return memberService.getAllMembers();
    }

    @GetMapping("/members/{memberId}")
    public MemberDto.Response getMemberDetail(
            @PathVariable final String memberId
    ) throws Exception {
        return memberService.getMemberDetail(memberId);
    }

    @PutMapping("/members/{memberId}")
    public String editMember(
            @PathVariable final String memberId,
            @Valid @RequestBody MemberDto.Request request
    ) throws Exception {
        return memberService.editMember(memberId, request);
    }

    @DeleteMapping("/members/{memberId}")
    public void deleteMember(
            @PathVariable final String memberId
    ){
        log.info(memberService.deleteMember(memberId));
    }
}