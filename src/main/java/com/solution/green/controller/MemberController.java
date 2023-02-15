package com.solution.green.controller;

import com.google.cloud.firestore.DocumentReference;
import com.solution.green.entity.CreateMember;
import com.solution.green.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@Slf4j // log
@CrossOrigin(origins = "*")
public class MemberController {
    @Autowired
    UserService userService;

    @PostMapping("/create-member")
    public String createMember(@Valid @RequestBody CreateMember.Request request)
            throws Exception {
        log.info("[request]: {}", request);
        return userService.createMember(request);
    }

    @GetMapping("/members")
    public List<CreateMember.Response> getAllMembers() throws Exception{
        return userService.getAllMembers();
    }

    @GetMapping("/members/{memberId}")
    public CreateMember.Response getMemberDetail(
            @PathVariable final String memberId
    ) throws Exception {
        return userService.getMemberDetail(memberId);
    }

    @PutMapping("/members/{memberId}")
    public String editMember(
            @PathVariable final String memberId,
            @Valid @RequestBody CreateMember.Request request
    ) throws Exception {
        return userService.editMember(memberId, request);
    }

    @DeleteMapping("/members/{memberId}")
    public void deleteMember(
            @PathVariable final String memberId
    ){
        log.info(userService.deleteMember(memberId));
    }
}