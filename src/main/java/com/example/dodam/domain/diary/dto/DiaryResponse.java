package com.example.dodam.domain.diary.dto;

import com.example.dodam.domain.diary.Diary;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

//다이어리 목록 조회할 때 사용
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DiaryResponse {

    public Long id;

    public Date date;

    public String feel;

    public static List<DiaryResponse> listOf(List<Diary> diaries) {
        return diaries.stream()
            .map(DiaryResponse::of)
            .collect(Collectors.toList());
    }

    public static DiaryResponse of(Diary diary) {
        return DiaryResponse.builder()
            .id(diary.getId())
            .date(diary.getDate())
            .feel(diary.getFeel())
            .build();
    }
}

