package com.example.dodam.repository.member;

import com.example.dodam.domain.member.Member;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    void deleteById(Long userId);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findById(Long id);
}
