package com.solution.green.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member_category")
public class MemberCategory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mem_cate_id", length = 20)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "cate_id_1", referencedColumnName = "cate_id")
    private Category firstCategory;

    @ManyToOne
    @JoinColumn(name = "cate_id_2", referencedColumnName = "cate_id")
    private Category secondCategory;
    @ManyToOne
    @JoinColumn(name = "cate_id_3", referencedColumnName = "cate_id")
    private Category thirdCategory;
    @ManyToOne
    @JoinColumn(name = "cate_id_4", referencedColumnName = "cate_id")
    private Category fourthCategory;

    // TODO - AuditingEntityListener 사용해야함
}