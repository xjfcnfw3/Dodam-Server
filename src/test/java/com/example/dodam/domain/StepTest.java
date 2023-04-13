package com.example.dodam.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.example.dodam.domain.member.Member;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StepTest {

    private Step step;

    @BeforeEach
    void init() {
        step = Step.builder()
            .stepId(1L)
            .stepName("시작")
            .stepOrder(1)
            .startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(1))
            .build();
    }

    @DisplayName("단계를 업데이트 한다.")
    @Test
    void update() {
        Step request = Step.builder()
            .stepName("수정")
            .startDate(LocalDate.now().plusDays(1))
            .endDate(LocalDate.now().plusDays(2))
            .build();

        step.update(request);

        assertAll(
            () -> assertThat(step.getStepName()).isEqualTo(request.getStepName()),
            () -> assertThat(step.getStartDate()).isEqualTo(request.getStartDate()),
            () -> assertThat(step.getEndDate()).isEqualTo(request.getEndDate())
        );
    }


    @DisplayName("단계를 수정한다.")
    @Test
    void changeOrder() {
        int changedOrder = 2;

        step.changeOrder(changedOrder);

        assertThat(step.getStepOrder()).isEqualTo(changedOrder);
    }

    @DisplayName("유저와 연관짓는다")
    @Test
    void associate() {
        Member member = Member.builder()
            .id(1L)
            .build();
        step.associateMember(member);

        assertAll(
            () -> assertThat(step.getMember()).isEqualTo(member),
            () -> assertThat(member.getSteps()).hasSize(1)
        );
    }
}
