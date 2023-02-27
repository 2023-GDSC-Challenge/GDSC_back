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
@Table(name = "sub_categories")
public class SubCategories implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sub_cate_id", length = 20)
    private Long id;
    @Column(name = "sub_cate_name", length = 50, nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "parent_cate_id", referencedColumnName = "cate_id")
    private Category category;
    // TODO - AuditingEntityListener 사용해야함
}