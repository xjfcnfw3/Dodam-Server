package com.example.dodam.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.dodam.domain.member.UpdateMemberRequest;
import com.example.dodam.domain.member.Member;
import com.example.dodam.repository.member.MemberRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private DataSource dataSource;
    private JdbcTemplate template;

    @BeforeEach
    void initGeneratedId() {
        template = new JdbcTemplate(dataSource);
        template.execute("ALTER TABLE USER auto_increment = 1");
    }

    @DisplayName("Test user save")
    @MethodSource("generateTestUsers")
    @ParameterizedTest
    void save(Member member) {
        Member savedMember = memberRepository.save(member);
        Member foundMember = memberRepository.findById(member.getId()).get();
        assertThat(foundMember.getId()).isEqualTo(savedMember.getId());
        assertThat(foundMember.getCreateAt()).isNotNull();
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

    @DisplayName("Test update user")
    @MethodSource("generateUpdateTestArguments")
    @ParameterizedTest
    void update(Member member, UpdateMemberRequest userDto) {
        Member savedMember = memberRepository.save(member);
        memberRepository.update(member.getId(), userDto.toUser());
        Member updatedMember = memberRepository.findById(savedMember.getId()).get();
        assertThat(updatedMember.getUpdateAt()).isNotNull();
        updatedMember.setUpdateAt(null);
        assertThat(updatedMember.toString()).isNotEqualTo(savedMember.toString());
    }

    @DisplayName("Test find user by nickname")
    @MethodSource("generateTestUsers")
    @ParameterizedTest
    void findByNickName(Member member) {
        memberRepository.save(member);
        Member savedMember = memberRepository.findByNickName("test").get();
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
                .phone("01000000000").password("123").status("A").build()
            )
        );
    }

    private static Stream<Arguments> generateUpdateTestArguments() {
        return Stream.of(
            Arguments.arguments(
                Member.builder().email("test@naver.com").nickname("test").role("ROLE_USER")
                        .phone("01012345678").password("123").status("A").build(),
                UpdateMemberRequest.builder().nickname("tester").role("ROLE_USER")
                    .phone("01000000000").password("1234").status("A").build()
            )
        );
    }
}
