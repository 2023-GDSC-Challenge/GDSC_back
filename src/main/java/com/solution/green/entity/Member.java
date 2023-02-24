package com.solution.green.entity;

import javax.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;

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

    @Column(name = "member_email", length = 50, nullable = false, unique = true)
    private String email;

    @Column(name = "member_nickname", length = 30, nullable = false)
    private String nickname;

    @Column(name = "member_password", length = 50, nullable = false)
    private String password;

    @Column(name = "member_residence", length = 100, nullable = true)
    private String residence;

    @Column(name = "member_image", nullable = true)
    private String image;

    // TODO - AuditingEntityListener 사용해야함
}