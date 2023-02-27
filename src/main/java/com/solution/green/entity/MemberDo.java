package com.solution.green.entity;

import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "member_do")
public class MemberDo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "mem_do_id", length = 20)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", referencedColumnName = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "quest_id", referencedColumnName = "quest_id")
    private Quest quest;

    @Column(name = "start_date", nullable = true)
    private Date startDate;
    @Column(name = "due_date", nullable = true)
    private Date dueDate;

    @Column(name = "stance", nullable = false)
    private int stance;
    /*
     * 0: 찜
     * 1: 진행중
     * 2: 완료
     * */

    // TODO - AuditingEntityListener 사용해야함
}