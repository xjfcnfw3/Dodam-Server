package com.example.dodam.repository.member;

import com.example.dodam.domain.member.Member;

import java.time.LocalDateTime;
import java.util.Optional;

public interface MemberRepository {
	Optional<Member> findByUsername(String email);
    Member save(Member member);
    Optional<Member> findByEmail(String email);
    void deleteById(Long userId);
    Member update(Long userId, Member member);
    Optional<Member> findByNickName(String nickname);
    Optional<Member> findById(Long id);
    void deleteImage(Long userId);
    void updateStartDate(Long userId, LocalDateTime startDate);
}
