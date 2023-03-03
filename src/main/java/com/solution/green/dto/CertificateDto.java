package com.solution.green.dto;

import com.solution.green.entity.Category;
import com.solution.green.entity.CertificateImage;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

public class CertificateDto {
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class DetailView {
        @NotNull
        private Long id;
        @NotNull
        private Date submitDate;
        @NotNull
        private String image;

        public static DetailView fromEntity(CertificateImage certificateImage) {
            return DetailView.builder()
                    .id(certificateImage.getId())
                    .submitDate(certificateImage.getSubmitDate())
                    .image(certificateImage.getImage())
                    .build();
        }
    }
}
