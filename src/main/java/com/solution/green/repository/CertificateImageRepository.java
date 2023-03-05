package com.solution.green.repository;

import com.solution.green.dto.CertificateDto;
import com.solution.green.entity.CertificateImage;
import com.solution.green.entity.MemberDo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificateImageRepository extends JpaRepository<CertificateImage, Long> {

    List<CertificateImage> findByMemberDo_IdOrderBySubmitDateAsc(Long id);

    long deleteByMemberDo(MemberDo memberDo);
}