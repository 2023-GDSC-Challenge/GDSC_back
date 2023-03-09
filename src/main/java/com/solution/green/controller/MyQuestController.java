package com.solution.green.controller;

import com.solution.green.dto.CertificateDto;
import com.solution.green.dto.MemDoDto;
import com.solution.green.service.CertificateService;
import com.solution.green.service.GCSService;
import com.solution.green.service.MemCateService;
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
    private final MemCateService memCateService;
    private final GCSService gcsService;
    private final CertificateService certificateService;


    @GetMapping("/getMyQuestNotYetList/{memberId}")
    public List<MemDoDto.ListView> getMyQuestNotYetList(@PathVariable final Long memberId) {
        return memDoService.getMyQuestNotYetList(memberId);
    }

    @GetMapping("/getMyQuestIngList/{memberId}")
    public List<MemDoDto.ListView> getMyQuestIngList(@PathVariable final Long memberId) {
        return memDoService.getMyQuestIngList(memberId);
    }

    @GetMapping("/getMyQuestDoneList/{memberId}")
    public List<MemDoDto.ListView> getMyQuestDoneList(@PathVariable final Long memberId) {
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
        // save image to cloud
        certificateService.updateCertificateImage(memberDoId, uuid);
        // check if quest is finished
        memDoService.validateQuestIsDone(memberDoId);
        // return saved URL
        return URL_PREFIX.getDescription() + uuid;
    }

    @PatchMapping("/updateQuestGiveUp/{memberDoId}") // TODO - not yet testing
    public void updateQuestGiveUp(@PathVariable final Long memberDoId) {
        memDoService.deleteQuest(memberDoId);
    }
}