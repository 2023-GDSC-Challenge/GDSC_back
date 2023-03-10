package com.solution.green.controller;

import com.solution.green.dto.MemCateDto;
import com.solution.green.dto.MemGetDto;
import com.solution.green.dto.MemberDto;
import com.solution.green.service.GCSService;
import com.solution.green.service.MemCateService;
import com.solution.green.service.MemGetService;
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
    private final MemGetService memGetService;

    @PostMapping("/create-member")
    public MemberDto.Response createMember(
            @Valid @RequestBody MemberDto.Request request) {
        return memberService.createMember(request);
    }

    @PostMapping("/createPriority/{memberId}")
    public void createPriority(@PathVariable final Long memberId,
                               @Valid @RequestBody MemCateDto.Request request) {
        memCateService.createPriority(memberId, request);
    }

    @PostMapping("/createTitle/{memberId}")
    public MemGetDto.Title createTitle(@PathVariable final Long memberId,
                                       @Valid @RequestBody Long titleId) {
        return memGetService.createTitle(memberId, titleId);
    }

    @DeleteMapping("/delete-member/{memberId}")
    public void deleteMember(@PathVariable final Long memberId) {
        memberService.deleteMember(memberId);
    }

    @GetMapping("/get-all-members")
    public List<MemberDto.Response> getAllMembers() {
        return memberService.getAllMembers();
    }

    @GetMapping("/get-member-detail/{memberId}")
    public MemberDto.Response getMemberDetail(@PathVariable final Long memberId) {
        return memberService.getMemberDetail(memberId);
    }

    @PostMapping("/login")
    public MemberDto.ToModel login(@Valid @RequestBody MemberDto.Login loginMember) {
        return memberService.login(loginMember);
    }

    @PutMapping("/update-member/{memberId}")
    public MemberDto.Response updateMember(
            @PathVariable final Long memberId,
            @Valid @RequestBody MemberDto.Request request) {
        return memberService.updateMember(memberId, request);
    }

    @PatchMapping("/update-member-image/{memberId}")
    public String updateMemberImage(@PathVariable final Long memberId,
                                    @RequestPart(value = "file") MultipartFile file)
            throws IOException {
        String newUrl = URL_PREFIX.getDescription() + gcsService.uploadImage(file);
        memberService.updateMemberImage(memberId, newUrl);
        return newUrl;
    }

    @PatchMapping("/updatePriority/{memberId}")
    public void updatePriority(@PathVariable final Long memberId,
                               @Valid @RequestBody MemCateDto.Request request) {
        memCateService.updatePriority(memberId, request);
    }

    @PatchMapping("/updateTitle/{memberId}")
    public MemGetDto.Title updateTitle(@PathVariable final Long memberId,
                                       @Valid @RequestBody Long titleId) {
        return memGetService.updateTitle(memberId, titleId);
    }
}