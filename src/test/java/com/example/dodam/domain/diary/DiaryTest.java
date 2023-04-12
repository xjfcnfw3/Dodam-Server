package com.example.dodam.domain.diary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.dodam.domain.member.Member;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DiaryTest {

    private Diary diary;

    @BeforeEach
    void init() {
        diary = Diary.builder()
            .title("오늘의 일")
            .content("오늘은 노래를 들려주었어요.")
            .talkToBaby("이 노래 좋니?")
            .date(LocalDate.now())
            .feel("Good")
            .imgPath("/hello")
            .build();
    }

    @DisplayName("변경된 일기장 이미지 경로로 업데이트")
    @Test
    void updateImagePath() {
        String updatedPath = "/new";
        diary.updateImagePath(updatedPath);
        assertThat(diary.getImgPath()).isEqualTo(updatedPath);
    }

    @DisplayName("일기장과 유저와 연관관계를 맺는다.")
    @Test
    void associate() {
        Member member = new Member();
        diary.associateMember(member);
        assertThat(member.getDiaries().get(0)).isEqualTo(diary);
    }

    @DisplayName("일기장 내용이 변경된다.")
    @Test
    void updateDiaryContent() {
        Diary updateDiary = Diary.builder()
            .title("병원에 갔어요")
            .content("오늘은 시술하는 날이에요")
            .talkToBaby("아기야 힘내")
            .date(LocalDate.now())
            .feel("Hard")
            .build();
        diary.updateDiaryContent(updateDiary);

        assertAll(
            () -> assertThat(diary.getContent()).isEqualTo(updateDiary.getContent()),
            () -> assertThat(diary.getTitle()).isEqualTo(updateDiary.getTitle()),
            () -> assertThat(diary.getDate()).isEqualTo(updateDiary.getDate()),
            () -> assertThat(diary.getFeel()).isEqualTo(updateDiary.getFeel()),
            () -> assertThat(diary.getTalkToBaby()).isEqualTo(updateDiary.getTalkToBaby())
        );
    }
}
