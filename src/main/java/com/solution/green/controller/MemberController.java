package com.solution.green.controller;

import com.google.firebase.database.annotations.NotNull;
import com.solution.green.dto.MemberDto;
import com.solution.green.service.MemberService;
import lombok.*;
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

    @GetMapping("/getTest")
    public String getTest(){
        return "Get Mapping Test Success!";
    }
    @PostMapping("/postTest")
    public String postTest(@RequestBody String name){
        return "test name is: " + name;
    }

    @GetMapping("/rde-testing")
    public void rdsTesting(){
        memberService.testing();
        System.out.println("done!");
    }





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
    ) throws Exception {return memberService.getMemberDetail(memberId);}
    @PutMapping("/members/{memberId}")
    public String editMember(
            @PathVariable final String memberId,
            @Valid @RequestBody MemberDto.Request request
    ) throws Exception {return memberService.editMember(memberId, request);}
    @DeleteMapping("/members/{memberId}")
    public void deleteMember(
            @PathVariable final String memberId
    ){log.info(memberService.deleteMember(memberId));}

    @PostMapping("/login")
    public MemberDto.Response login(@Valid @RequestBody MemberDto.login loginMember)
            throws Exception {return memberService.login(loginMember);}
}