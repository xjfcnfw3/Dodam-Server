package com.example.dodam.service.member;

import com.example.dodam.common.exception.EntityNotFoundException;
import com.example.dodam.common.exception.ErrorCode;
import com.example.dodam.domain.member.UpdateMemberRequest;
import com.example.dodam.domain.member.Member;
import com.example.dodam.repository.member.MemberRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member getUser(String email) {
        return memberRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public void update(String email, UpdateMemberRequest updateMemberRequest) {
        Member member = memberRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public void delete(Long userId) {
        memberRepository.deleteById(userId);
    }

    public void deleteImage(Long userId) {
        Optional<Member> foundMember = memberRepository.findById(userId);
        if (foundMember.isPresent()) {
            Member member  = foundMember.get();
            member.setImgPath("");
            memberRepository.save(member);
        }
    }
}
