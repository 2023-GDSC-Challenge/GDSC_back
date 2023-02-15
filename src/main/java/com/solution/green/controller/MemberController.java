package com.solution.green.controller;

import com.google.cloud.firestore.DocumentReference;
import com.solution.green.entity.CreateMember;
import com.solution.green.service.UserService;
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


//    @GetMapping("/developers")
//    public List<DeveloperDto> getAllDevelopers() {
//        log.info("GET /developers HTTP/1.1");
//        return dMakerService.getAllEmployedDevelopers();
//    }
//
//    @GetMapping("/developers/{memberId}")
//    public DeveloperDetailDto getDeveloperDetail(
//            @PathVariable final String memberId // 입력받은 값은 바뀌지 않음 - final
//    ) {
//        log.info("GET /developers HTTP/1.1");
//
//        return dMakerService.getDeveloperDetail(memberId);
//    }
//
//
//    @PutMapping("/developers/{memberId}")
//    public DeveloperDetailDto editDeveloper(
//            @PathVariable final String memberId, // for [Data binding]
//            @Valid @RequestBody EditDeveloper.Request request
//    ) {
//        log.info("GET /developers HTTP/1.1");
//
//        return dMakerService.editDeveloper(memberId, request);
//    }
//
//    @DeleteMapping()
//    public DeveloperDetailDto deleteDeveloper(
//            @PathVariable final String memberId
//    ){
//        return dMakerService.deleteDeveloper(memberId);
//    }
}