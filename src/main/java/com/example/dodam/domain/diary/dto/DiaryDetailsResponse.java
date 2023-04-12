package com.example.dodam.domain.diary.dto;

import com.example.dodam.domain.diary.Diary;
import java.time.LocalDate;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

//다이어리 디테일 정보 반환할 때 사용
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class DiaryDetailsResponse {

    private Long id;
    private LocalDate date;
    private String title;
    private String imgPath;
    private String talkToBaby;
    private String feel;
    private String content;

    public static DiaryDetailsResponse of(Diary diary) {
        return DiaryDetailsResponse.builder()
            .id(diary.getId())
            .date(diary.getDate())
            .title(diary.getTitle())
            .imgPath(diary.getImgPath())
            .talkToBaby(diary.getTalkToBaby())
            .feel(diary.getFeel())
            .content(diary.getContent())
            .build();
    }
}
