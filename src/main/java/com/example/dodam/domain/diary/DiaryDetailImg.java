package com.example.dodam.domain.diary;

import lombok.Getter;
import lombok.Setter;

//다이어리 디테일 정보 반환할 때 사용
@Getter
@Setter
public class DiaryDetailImg {
    public Long id;
    public String img;
    public String title;
    public String oneWord;
    public String content;
}
