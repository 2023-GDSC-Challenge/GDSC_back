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
@Table(name = "badges")
public class Badge implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id", length = 20)
    private Long id;

    @Column(name = "badge_name", length = 100, nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "cate_id", referencedColumnName = "cate_id", nullable = true)
    private Category category;

    @Column(name = "achievement", length = 20, nullable = true)
    private Double achievement;
    // TODO - AuditingEntityListener 사용해야함
}