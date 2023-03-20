package com.example.dodam.domain.diary;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
//다이어리 수정할 때 기존 저장된 이미지 위치 찾기 위해 사용 (findDiary 메소드)
@Getter
@Setter
public class DiaryDetail {
    public Integer id;
    public String title;
    public String imgPath;
    public String oneWord;
    public String content;


}
