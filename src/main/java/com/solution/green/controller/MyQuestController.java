package com.solution.green.controller;

import com.solution.green.dto.CertificateDto;
import com.solution.green.dto.MemDoDto;
import com.solution.green.service.CertificateService;
import com.solution.green.service.GCSService;
import com.solution.green.service.MemDoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.solution.green.code.DatabaseName.URL_PREFIX;

@RestController
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/api")
public class MyQuestController {
    private final MemDoService memDoService;
    private final GCSService gcsService;
    private final CertificateService certificateService;


    @GetMapping("/getMyQuestNotYetList/{memberId}")
    public List<MemDoDto.My> getMyQuestNotYetList(@PathVariable final Long memberId) {
        return memDoService.getMyQuestNotYetList(memberId);
    }

    @GetMapping("/getMyQuestIngList/{memberId}")
    public List<MemDoDto.My> getMyQuestIngList(@PathVariable final Long memberId) {
        return memDoService.getMyQuestIngList(memberId);
    }

    @GetMapping("/getMyQuestDoneList/{memberId}")
    public List<MemDoDto.My> getMyQuestDoneList(@PathVariable final Long memberId) {
        return memDoService.getMyQuestDoneList(memberId);
    }

    @GetMapping("/getMyQuestDetailView/{memberDoId}")
    public MemDoDto.DetailView getMyQuestDetailView(
            @PathVariable final Long memberDoId) {
        return memDoService.getMyQuestDetailView(memberDoId);
    }

    @GetMapping("/getCertificateImages/{memberDoId}")
    public List<CertificateDto.DetailView> getCertificateImages(
            @PathVariable final Long memberDoId) {
        return certificateService.getCertificateImages(memberDoId);
    }

    @PatchMapping("/uploadCertificateImage/{memberDoId}")
    public String uploadCertificateImage(
            @PathVariable final Long memberDoId,
            @RequestPart(value = "file") MultipartFile file)
            throws IOException {
        String uuid = gcsService.uploadImage(file);
        certificateService.updateCertificateImage(memberDoId, uuid);
        return URL_PREFIX.getDescription() + uuid;
    }

    // TODO - 퀘스트 완료했을 때 완료 처리하는 메소드
    @PatchMapping("/updateQuestDone/{memberDoId}")
    public String updateQuestDone(@PathVariable final Long memberDoId) {
        // TODO - 멤버두 stance 를 사용 완료로 변경

        // TODO - 업데이트되는 뱃지 있는지 확인
        // TODO - 업데이트된 뱃지가 있으면 마이겟 디비에 추가
        return null;
    }

    @PatchMapping("/updateQuestGiveUp/{memberDoId}") // TODO - not yet testing
    public String updateQuestGiveUp(@PathVariable final Long memberDoId) {
        memDoService.deleteQuest(memberDoId);
        return null;
    }
}