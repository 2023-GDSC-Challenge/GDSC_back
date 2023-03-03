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
@Table(name = "quests")
public class Quest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quest_id", length = 20)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sub_cate_id", referencedColumnName = "sub_cate_id")
    private SubCategories subCategory;

    @Column(name = "quest_name", length = 100, nullable = false)
    private String name;

    @Column(name = "quest_reward", length = 20, nullable = false)
    private Integer reward;

    @Column(name = "briefing", length = 200, nullable = true)
    private String briefing;
    @Column(name = "information", length = 200, nullable = true)
    private String information;

    @Column(name = "quest_time_limit", length = 20, nullable = true)
    private Integer timeLimit;

    @Column(name = "challenger", length = 20, nullable = true)
    private Integer challenger;

    // TODO - AuditingEntityListener 사용해야함
}