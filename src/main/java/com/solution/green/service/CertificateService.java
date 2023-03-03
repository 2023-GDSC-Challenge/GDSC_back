package com.solution.green.service;

import com.solution.green.dto.CertificateDto;
import com.solution.green.entity.CertificateImage;
import com.solution.green.exception.GreenException;
import com.solution.green.repository.CertificateImageRepository;
import com.solution.green.repository.MemDoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.solution.green.code.DatabaseName.URL_PREFIX;
import static com.solution.green.code.GreenErrorCode.NO_MEM_DO;

@Service
@RequiredArgsConstructor
public class CertificateService {
    private final CertificateImageRepository certificateImageRepository;
    private final MemDoRepository memDoRepository;

    @Transactional
    public void updateCertificateImage(Long memberDoId, String uuid) {
        certificateImageRepository.save(
                CertificateImage.builder()
                        .memberDo(memDoRepository.findById(memberDoId)
                                .orElseThrow(() -> new GreenException(NO_MEM_DO)))
                        .submitDate(new Date())
                        .image(uuid)
                        .build()
        );
    }

    // TODO - 깊은 복사인지 확인해야함
    public List<CertificateDto.DetailView> getCertificateImages(Long memberDoId) {
        List<CertificateDto.DetailView> list =
                certificateImageRepository
                        .findByMemberDo_IdOrderBySubmitDateAsc(memberDoId)
                        .stream()
                        .map(CertificateDto.DetailView::fromEntity)
                        .collect(Collectors.toList());
        List<CertificateDto.DetailView> finalList = new ArrayList<>();
        for (CertificateDto.DetailView dto : list) {
            dto.setImage(URL_PREFIX.getDescription() + dto.getImage());
            finalList.add(dto);
        }
        return finalList;
    }
}
