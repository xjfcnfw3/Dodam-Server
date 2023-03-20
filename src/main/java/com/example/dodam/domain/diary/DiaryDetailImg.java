package com.example.dodam.domain.diary;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
//다이어리 디테일 정보 반환할 때 사용
@Getter
@Setter
public class DiaryDetailImg {
    public Integer id;
    public String img;
    public String title;
    public String oneWord;
    public String content;
}
