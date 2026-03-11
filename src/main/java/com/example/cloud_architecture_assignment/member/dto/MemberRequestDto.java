package com.example.cloud_architecture_assignment.member.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;

@Getter
public class MemberRequestDto {
    @NotBlank(message = "이름은 필수 입력 값이며 공백일 수 없습니다.")
    private String name;
    @Positive(message = "나이는 양수여야 합니다.")
    private int age;
    @NotBlank(message = "mbti는 필수 입력 값이며 공백일 수 없습니다.")
    private String mbti;
}
