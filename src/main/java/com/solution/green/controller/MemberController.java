package com.solution.green.controller;

import com.solution.green.dto.MemCateDto;
import com.solution.green.dto.MemberDto;
import com.solution.green.service.GCSService;
import com.solution.green.service.MemCateService;
import com.solution.green.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static com.solution.green.code.DatabaseName.URL_PREFIX;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;
    private final GCSService gcsService;
    private final MemCateService memCateService;

    // TODO - 멤버 우선순위 카테고리 update
    // TODO - 현재 있는 뱃지 중 선택하는 메소드

    @PostMapping("/create-member")
    public MemberDto.Response createMember(
            @Valid @RequestBody MemberDto.Request request) {
        return memberService.createMember(request);
    }

    @PostMapping("/createPriority/{memberId}") // TODO - not yet checking
    public void createPriority(@PathVariable final Long memberId,
                               @Valid @RequestBody MemCateDto.Request request) {
        memCateService.createPriority(memberId, request);
    }
    @GetMapping("/get-all-members")
    public List<MemberDto.Response> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/get-member-detail/{memberId}")
    public MemberDto.Response getMemberDetail(@PathVariable final Long memberId) {
        return memberService.getMemberDetail(memberId);
    }

    @GetMapping("/get-user-image/{memberId}")
    public String getUserImage(@PathVariable final Long memberId) {
        return URL_PREFIX.getDescription() + memberService.getUserImageURL(memberId);
    }

    @PatchMapping("/update-member-image/{memberId}")
    public String updateMemberImage(@PathVariable final Long memberId,
                                    @RequestPart(value = "file") MultipartFile file)
            throws IOException {
        String uuid = gcsService.uploadImage(file);
        memberService.updateMemberImage(memberId, uuid);
        return URL_PREFIX.getDescription() + uuid;
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