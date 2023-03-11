package com.solution.green.entity;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

@DynamicInsert
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "members")
public class Member implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id", length = 20)
    private Long id;

    @Column(name = "email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "nickname", length = 30, nullable = false)
    private String nickname;

    @Column(name = "password", length = 50, nullable = false)
    private String password;

    @Column(name = "member_image")
    private String image;

    @Column(name = "member_reward", length = 20)
    @ColumnDefault("0")
    private Integer reward;

    @Column(name = "random_code", length = 50)
    private String randomCode;
    // TODO - AuditingEntityListener 사용해야함
}