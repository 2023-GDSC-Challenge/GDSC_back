package com.solution.green.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "certificate_image")
public class CertificateImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certi_image_id", length = 20)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mem_do_id", referencedColumnName = "mem_do_id")
    private MemberDo memberDo;

    @Column(name = "submit_date")
    private Date submitDate;

}
