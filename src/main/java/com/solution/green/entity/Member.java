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
    @Column(name = "user_id", length = 20)
    private Long id;

    @Column(name = "user_name", length = 50, nullable = false)
    private String name;

    @Column(name = "user_email", length = 100, nullable = false, unique = true)
    private String email;

    @Column(name = "user_password", length = 200, nullable = false)
    private String password;

    @Column(name = "user_image", nullable = true)
    private String image;

    // TODO - AuditingEntityListener 사용해야함
}