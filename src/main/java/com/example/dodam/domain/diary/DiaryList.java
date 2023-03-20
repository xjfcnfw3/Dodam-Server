package com.example.dodam.domain.diary;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
//다이어리 목록 조회할 때 사용
@Getter
@Setter
public class DiaryList {
    public Integer id;
    public Date date;
    public String feel;

}

