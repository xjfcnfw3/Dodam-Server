package com.example.dodam.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.dodam.domain.member.UpdateMemberRequest;
import com.example.dodam.domain.member.Member;
import com.example.dodam.repository.member.MemberRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Test user save")
    @MethodSource("generateTestUsers")
    @ParameterizedTest
    void save(Member member) {
        Member savedMember = memberRepository.save(member);
        Optional<Member> foundMember = memberRepository.findById(savedMember.getId());
        assertThat(foundMember.isPresent()).isTrue();
    }

    @DisplayName("Test find user by email")
    @MethodSource("generateTestUsers")
    @ParameterizedTest
    void findByEmail(Member member) {
        memberRepository.save(member);
        Member savedMember = memberRepository.findByEmail("test@naver.com").get();
        assertThat(member.getId()).isEqualTo(savedMember.getId());
    }

    @DisplayName("Test delete user")
    @MethodSource("generateTestUsers")
    @ParameterizedTest
    void deleteById(Member member) {
        memberRepository.save(member);
        memberRepository.deleteById(member.getId());
        Optional<Member> userOptional = memberRepository.findById(member.getId());
        assertThatThrownBy(userOptional::get)
            .isInstanceOf(NoSuchElementException.class);
    }


    @DisplayName("Test find user by nickname")
    @MethodSource("generateTestUsers")
    @ParameterizedTest
    void findByNickName(Member member) {
        memberRepository.save(member);
        Member savedMember = memberRepository.findByNickname("test").get();
        assertThat(savedMember.getId()).isEqualTo(member.getId());
    }

    @DisplayName("Test find users by id")
    @MethodSource("generateTestUsers")
    @ParameterizedTest
    void findById(Member member) {
        memberRepository.save(member);
        Member savedMember = memberRepository.findById(member.getId()).get();
        assertThat(savedMember.getNickname()).isEqualTo(member.getNickname());
    }

    private static Stream<Arguments> generateTestUsers() {
        return Stream.of(
            Arguments.arguments(Member.builder().email("test@naver.com").nickname("test").role("ROLE_USER")
                .phone("01000000000").password("12345678").status("A").build()
            )
        );
    }
}
