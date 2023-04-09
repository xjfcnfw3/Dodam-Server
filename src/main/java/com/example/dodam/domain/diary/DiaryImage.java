package com.example.dodam.domain.diary;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

//다이어리 등록, 다이어리 수정 요청 body에 사용
@Getter
@Setter
public class DiaryImage {
    public Long id;
    public Long userId;
    public Date date;
    public String title;
    public String base64Img;
    public String oneWord;
    public String feel;
    public String content;

}
