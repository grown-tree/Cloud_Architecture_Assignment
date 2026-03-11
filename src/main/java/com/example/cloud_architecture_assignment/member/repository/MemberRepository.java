package com.example.cloud_architecture_assignment.member.repository;

import com.example.cloud_architecture_assignment.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    //멤버 전체조회
    List<Member> findAll();
}
