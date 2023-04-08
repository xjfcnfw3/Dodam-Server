package com.example.dodam.service.member;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.dodam.domain.member.Member;
import com.example.dodam.domain.member.UpdateMemberRequest;
import com.example.dodam.repository.member.MemberRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void init() {
        memberService = new MemberService(memberRepository);
        member = Member.builder()
            .id(1L)
            .email("test@naver.com")
            .nickname("tester")
            .phone("01000000000")
            .build();
    }

    @Test
    void getUser() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));

        Member foundMember = memberService.getUser(member.getEmail());

        assertThat(foundMember.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    void update() {
        when(memberRepository.findByEmail(anyString())).thenReturn(Optional.of(member));
        UpdateMemberRequest request = UpdateMemberRequest.builder()
            .password("87654321")
            .nickname("update")
            .startDate(LocalDateTime.now())
            .build();
        memberService.update(member.getEmail(), request);

        verify(memberRepository).findByEmail(anyString());
    }

    @Test
    void delete() {
        memberService.delete(member.getId());
        verify(memberRepository).deleteById(any());
    }

    @Test
    void deleteImage() {
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        memberService.deleteImage(member.getId());
        verify(memberRepository).findById(any());
    }
}
