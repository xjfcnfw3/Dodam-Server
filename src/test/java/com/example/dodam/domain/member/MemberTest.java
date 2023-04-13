package com.example.dodam.domain.member;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.dodam.domain.Step;
import com.example.dodam.domain.diary.Diary;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MemberTest {

    private Member member;
    private Diary diary;
    private Step step;

    @BeforeEach
    void init() {
        member = Member.builder()
            .id(1L)
            .nickname("tester")
            .password("12345678")
            .role("ROLE_USER")
            .email("hello@naver.com")
            .phone("010-1234-5678")
            .startDate(LocalDate.now())
            .build();

        diary = Diary.builder()
            .title("안녕")
            .content("테스트")
            .build();

        step = Step.builder()
            .stepName("test")
            .build();
    }

    @DisplayName("일기장이 추가되면 리스트에 저장된다.")
    @Test
    void addDiary() {
        member.addDiary(diary);

        assertThat(member.getDiaries()).containsExactly(diary);
    }

    @DisplayName("일기장을 삭제한다.")
    @Test
    void deleteDiary() {
        member.addDiary(diary);
        member.deleteDiary(diary);

        assertThat(member.getDiaries()).isEmpty();
    }

    @DisplayName("단계를 추가한다.")
    @Test
    void addStep() {
        member.addStep(step);

        assertThat(member.getSteps()).containsExactly(step);
    }

    @DisplayName("단계를 삭제한다.")
    @Test
    void deleteStep() {
        member.addStep(step);
        member.deleteStep(step);

        assertThat(member.getSteps()).isEmpty();
    }
}
