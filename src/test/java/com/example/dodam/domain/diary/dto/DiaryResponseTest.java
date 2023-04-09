package com.example.dodam.domain.diary.dto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.example.dodam.domain.diary.Diary;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DiaryResponseTest {

    @DisplayName("다이어리를 Response로 변환한다.")
    @Test
    void makeDiaryResponse() {
        Diary diary = Diary.builder().id(1L).title("일기장").content("일기장입니다.").build();
        DiaryResponse response = DiaryResponse.of(diary);
        assertAll(
            () -> assertThat(response.getDate()).isEqualTo(diary.getDate()),
            () -> assertThat(response.getId()).isEqualTo(diary.getId()),
            () -> assertThat(response.getFeel()).isEqualTo(diary.getFeel())
        );
    }

    @DisplayName("여러 다이어리를 Response 리스트로 변환")
    @Test
    void makeDiaryResponses() {
        Diary diary1 = Diary.builder().id(1L).title("일기장1").content("일기장1입니다.").build();
        Diary diary2 = Diary.builder().id(2L).title("일기장2").content("일기장2입니다.").build();
        List<DiaryResponse> diaryResponses = DiaryResponse.listOf(List.of(diary1, diary2));
        assertThat(diaryResponses).hasSize(2);
    }
}
