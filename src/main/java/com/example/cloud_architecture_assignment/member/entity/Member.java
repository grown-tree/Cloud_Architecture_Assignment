package com.example.cloud_architecture_assignment.member.entity;

import io.micrometer.observation.annotation.Observed;
import jakarta.persistence.*;
import lombok.*;


@Getter
@Entity
@Table(name = "members")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String name;

    @Column(length = 10, nullable = false)
    private int age;

    @Column(length = 10, nullable = false)
    private String mbti;

}
