package com.example.dodam.domain.diary;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Diary {
    public Integer id;
    public Integer userId;
    public Date date;
    public String title;
    public String imgPath;
    public String oneWord;
    public String feel;
    public String content;
}
