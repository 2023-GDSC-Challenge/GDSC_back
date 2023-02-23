package com.solution.green.controller;

import com.solution.green.dto.MemberDto;
import com.solution.green.service.MemberService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@CrossOrigin(origins = "*")
public class MemberController {
    @GetMapping("/getTest")
    public String getTest() {
        return "Get Mapping Test Success!";
    }
    @PostMapping("/postTest")
    public String postTest(@RequestBody String name) {
        return "test name is: " + name;
    }
    /*-----------------------front connecting test-----------------------*/

    @Autowired
    MemberService memberService;

    @PostMapping("/create-member")
    public MemberDto.Response createMember(
            @Valid @RequestBody MemberDto.Request request) {
        return memberService.createMember(request);
    }

    @GetMapping("/members")
    public List<MemberDto.Response> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/members/{memberId}")
    public MemberDto.Response getMemberDetail(@PathVariable final Long memberId) {
        return memberService.getMemberDetail(memberId);
    }

    @PutMapping("/members/{memberId}")
    public MemberDto.Response editMember(
            @PathVariable final Long memberId,
            @Valid @RequestBody MemberDto.Request request) {
        return memberService.editMember(memberId, request);
    }

    @DeleteMapping("/members/{memberId}")
    public void deleteMember(@PathVariable final Long memberId) {
        memberService.deleteMember(memberId);
    }

    @PostMapping("/login")
    public MemberDto.Response login(@Valid @RequestBody MemberDto.Login loginMember) {
        return memberService.login(loginMember);
    }

}