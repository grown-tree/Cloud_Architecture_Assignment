package com.example.cloud_architecture_assignment.member.service;

import com.example.cloud_architecture_assignment.common.exception.FileUploadException;
import com.example.cloud_architecture_assignment.member.dto.MemberRequestDto;
import com.example.cloud_architecture_assignment.member.dto.MemberResponseDto;
import com.example.cloud_architecture_assignment.member.entity.Member;
import com.example.cloud_architecture_assignment.member.repository.MemberRepository;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    //Pre-signed URL의 유효 시간 7일로 설정
    private static final Duration PRESIGNED_URL_EXPIRATION = Duration.ofDays(7);
    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Transactional
    public MemberResponseDto saveMember(MemberRequestDto request) {
        log.info("[API - LOG] 멤버 생성 서비스 요청");

        Member member = Member.builder()
                .name(request.getName())
                .age(request.getAge())
                .mbti(request.getMbti())
                .build();

        Member savedMember = memberRepository.save(member);

        return new MemberResponseDto(
                savedMember.getId(),
                savedMember.getName(),
                savedMember.getAge(),
                savedMember.getMbti()
        );
    }

    //멤버id로 조회
    public MemberResponseDto getMemberById(Long id) {
        log.info("[API - LOG] " + id + "번 멤버 조회 서비스 요청");
        // 1. DB에서 ID로 조회 (없으면 예외 발생)
        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, id + "번인 멤버가 없습니다.")
                );

        // 2. Entity를 Dto로 변환하여 반환
        return new MemberResponseDto(
                member.getId(),
                member.getName(),
                member.getAge(),
                member.getMbti()
        );
    }

    //전체조회
    public List<MemberResponseDto> getAllMembers() {
        log.info("[API - LOG] 전체 멤버 정보 조회 서비스 요청");

        return memberRepository.findAll().stream()
                .map(member -> new MemberResponseDto(
                        member.getId(),
                        member.getName(),
                        member.getAge(),
                        member.getMbti()
                ))
                .collect(Collectors.toList());
    }

    //프로필 이미지 업로드
    @Transactional
    public String uploadProfileImage(Long id, MultipartFile file) {
        log.info("[API - LOG] " + id + "번 멤버 프로필이미지 업로드 서비스 요청");
        try {
            //S3에 저장될 파일의 경로와 고유한 이름(Key)생성
            String key = "uploads/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
            s3Template.upload(bucket, key, file.getInputStream());//key형식으로 만든 파일 업로드
            Member member = memberRepository.findById(id)
                    .orElseThrow(() ->
                            new ResponseStatusException(HttpStatus.NOT_FOUND, id + "번인 멤버가 없습니다.")
                    );
            member.updateProfileImage(key);
            memberRepository.save(member);

            return key;

        } catch (IOException e) {
            throw new FileUploadException("파일 업로드 실패",e);
        }
    }

    // 프로필 이미지 조회를 위한 Presigned URL 발급
    public String getPresignedUrl(Long id) {
        log.info("[API - LOG] " + id + "번 멤버 프로필이미지 조회 서비스 요청");
        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, id + "번인 멤버가 없습니다.")
                );

        String key = member.getProfileImageUrl(); // DB에 저장된 파일명(Key)

        return s3Template.createSignedGetURL(bucket, key, PRESIGNED_URL_EXPIRATION).toString();
    }
}
