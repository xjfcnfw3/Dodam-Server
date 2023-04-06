package com.example.dodam.domain.common;


import static org.assertj.core.api.Assertions.assertThat;

import com.example.dodam.config.JpaAuditingConfig;
import com.example.dodam.domain.member.Member;
import com.example.dodam.repository.member.MemberRepository;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@Import(JpaAuditingConfig.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
class BaseTimeEntityTest {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void init() {
        member = Member.builder()
            .email("test@naver.com")
            .password("12345678")
            .nickname("tester")
            .build();
    }

    @DisplayName("엔티티가 저장되면 createAt이 갱신된다.")
    @Test
    void saveEntityWithCreateAt() {
        memberRepository.save(member);
        assertThat(member.getCreateAt()).isNotNull();
    }

    @DisplayName("엔티티가 변경되면 updateAt이 갱신된다.")
    @Test
    void updateEntityWithUpdateAt() {
        Member savedMember = memberRepository.save(member);
        savedMember.setBirthDate(LocalDate.now());
        assertThat(savedMember.getUpdateAt()).isNotNull();
    }
}
