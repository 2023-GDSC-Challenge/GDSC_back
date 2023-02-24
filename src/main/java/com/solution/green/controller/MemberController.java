package com.solution.green.controller;

import com.solution.green.dto.MemberDto;
import com.solution.green.service.GCSService;
import com.solution.green.service.MemberService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class MemberController {
    @GetMapping("")
    public String home() {
        return "This is index page!\n" +
                "다른 페이지로 접속하기 위해서는 http://35.216.34.93:8080/api 뒷부분에\n" +
                "notion api 페이지에 있는 endpoint를 붙여주세요!";
    }
    @GetMapping("/getTest")
    public String getTest() {
        return "Get Mapping Test Success!";
    }
    @PostMapping("/postTest")
    public String postTest(@RequestBody String name) {
        return "test name is: " + name;
    }
    /*-----------------------front connecting test-----------------------*/

    private final MemberService memberService;
    private final GCSService gcsService;

    @PostMapping("/create-member")
    public MemberDto.Response createMember(
            @Valid @RequestBody MemberDto.Request request) {
        return memberService.createMember(request);
    }

    @GetMapping("/get-all-members")
    public List<MemberDto.Response> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/get-member-detail/{memberId}")
    public MemberDto.Response getMemberDetail(@PathVariable final Long memberId) {
        return memberService.getMemberDetail(memberId);
    }

    @GetMapping("/getUserImage/{memberId}")
    public String getUserImage(@PathVariable final Long userId) {
        return memberService.getUserImageURL(userId);
    }

    @PatchMapping("/update-member-image/{memberId}")
    public String updateMemberImage(@PathVariable final Long userId,
                                    @RequestPart(value="file") MultipartFile file)
            throws IOException {
        String uuid = gcsService.uploadImage(file);
        memberService.updateMemberImage(userId, gcsService.uploadImage(file));
        return uuid;
    }

    @PutMapping("/update-member/{memberId}")
    public MemberDto.Response updateMember(
            @PathVariable final Long memberId,
            @Valid @RequestBody MemberDto.Request request) {
        return memberService.updateMember(memberId, request);
    }

    @DeleteMapping("/delete-member/{memberId}")
    public void deleteMember(@PathVariable final Long memberId) {
        memberService.deleteMember(memberId);
    }

    @PostMapping("/login")
    public MemberDto.Response login(@Valid @RequestBody MemberDto.Login loginMember) {
        return memberService.login(loginMember);
    }

}