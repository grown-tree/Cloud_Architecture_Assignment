package com.example.cloud_architecture_assignment.member.service;

import com.example.cloud_architecture_assignment.common.exception.GlobalExceptionHandler;
import com.example.cloud_architecture_assignment.member.dto.MemberRequestDto;
import com.example.cloud_architecture_assignment.member.dto.MemberResponseDto;
import com.example.cloud_architecture_assignment.member.entity.Member;
import com.example.cloud_architecture_assignment.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponseDto saveMember(MemberRequestDto request) {
        log.info("[API - LOG] 멤버 생성 요청");

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
        log.info("[API - LOG] "+id+"번 멤버 조회 요청");
        // 1. DB에서 ID로 조회 (없으면 예외 발생)
        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND,id+"번인 멤버가 없습니다.")
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
        log.info("[API - LOG] 전체 멤버 정보 조회 요청");

        return memberRepository.findAll().stream()
                .map(member -> new MemberResponseDto(
                        member.getId(),
                        member.getName(),
                        member.getAge(),
                        member.getMbti()
                ))
                .collect(Collectors.toList());
    }
}
