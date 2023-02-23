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
@Table(name = "categories")
public class Category implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id", length = 20)
    private Long id;
    @Column(name = "category_name", length = 50, nullable = false, unique = true)
    private String name;
    @Column(name = "parent_category", length = 50, nullable = false)
    private String parentCategory;

    // TODO - AuditingEntityListener 사용해야함
}