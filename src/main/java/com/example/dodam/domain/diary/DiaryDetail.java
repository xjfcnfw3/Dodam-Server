package com.example.dodam.domain.diary;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//다이어리 수정할 때 기존 저장된 이미지 위치 찾기 위해 사용 (findDiary 메소드)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DiaryDetail {
    public Long id;
    public String title;
    public String imgPath;
    public String oneWord;
    public String content;

    public static DiaryDetail of(Diary diary) {
        return DiaryDetail.builder()
            .id(diary.getId())
            .oneWord(diary.getTalkToBaby())
            .content(diary.getContent())
            .imgPath(diary.getImgPath())
            .title(diary.getTitle())
            .build();
    }
}
