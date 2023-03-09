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

    /* TODO - 퀘스트 완료 처리 고민해야 함!
    *   1. 실패 -> questController에 만들자
    *       1) 자정마다 프론트에서 전체 함수 호출
    *           - 사진 개수 안 맞는 애들 포기(?)로 변경할 것
    *               - 그냥 myquest에서 삭제할 것
    *               - 근데 또 그냥 삭제하면 안됨 -> 삭제한 리스트 전송해줘야하는거아냐?
    *           - 해당 퀘스트 challenger -= 1
    *   2. 성공
    *       1) 사용자가 인증 사진을 업로드 할 경우 체크
    *           - 인증 사진 개수랑 같으면 -> updateQuestStance
    *   **** questDB 에 인증 사진 개수 attribute 추가할 것 */

    @PatchMapping("/updateQuestGiveUp/{memberDoId}") // TODO - not yet testing
    public String updateQuestGiveUp(@PathVariable final Long memberDoId) {
        memDoService.deleteQuest(memberDoId);
        return null;
    }
}