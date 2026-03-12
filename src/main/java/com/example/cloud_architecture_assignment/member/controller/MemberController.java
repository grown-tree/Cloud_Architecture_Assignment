package com.example.cloud_architecture_assignment.member.controller;

import com.example.cloud_architecture_assignment.member.dto.MemberRequestDto;
import com.example.cloud_architecture_assignment.member.dto.MemberResponseDto;
import com.example.cloud_architecture_assignment.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberController {

    private final MemberService memberService;

    // 멤버 정보 저장
    @PostMapping
    public ResponseEntity<MemberResponseDto> createMember(@Valid @RequestBody MemberRequestDto request) {
        log.info("[API - LOG] 멤버 정보 저장 요청: {}", request);

        MemberResponseDto response = memberService.saveMember(request);
        return ResponseEntity.ok(response);
    }

    // 멤버 정보 조회
    @GetMapping("/{id}")
    public ResponseEntity<MemberResponseDto> getMemberById(@PathVariable Long id) {
        log.info("[API - LOG] 멤버 정보 조회 요청 ID: {}", id);

        MemberResponseDto response = memberService.getMemberById(id);
        return ResponseEntity.ok(response);
    }

    // 전체 팀원 조회 API
    @GetMapping
    public ResponseEntity<List<MemberResponseDto>> getAllMembers() {
        log.info("[API - LOG] 전체 멤버 조회 호출");
        List<MemberResponseDto> members = memberService.getAllMembers();
        return ResponseEntity.ok(members);
    }

    //프로필 이미지 업로드
    @PostMapping("/{id}/profile-image")
    public ResponseEntity<String> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {
        log.info("[API - LOG] 프로필 이미지 업로드 요청 ID: {}", id);

        String imageUrl = memberService.uploadProfileImage(id, file);
        return ResponseEntity.ok(imageUrl);
    }

    // 프로필 이미지 조회를 위한 Presigned URL 발급
    @GetMapping("/{id}/profile-image")
    public ResponseEntity<String> getProfilePresignedUrl(@PathVariable Long id) {
        log.info("[API - LOG] Presigned URL 조회 요청 ID: {}", id);

        String presignedUrl = memberService.getPresignedUrl(id);
        return ResponseEntity.ok(presignedUrl);
    }




}
