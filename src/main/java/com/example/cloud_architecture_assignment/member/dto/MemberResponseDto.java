package com.example.cloud_architecture_assignment.member.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {
    private Long id;
    private String name;
    private int age;
    private String mbti;

}
