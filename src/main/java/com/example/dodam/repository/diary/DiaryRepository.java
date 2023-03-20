package com.example.dodam.repository.diary;

import com.example.dodam.domain.diary.Diary;
import com.example.dodam.domain.diary.DiaryDetail;
import com.example.dodam.domain.diary.DiaryList;

import java.util.List;
import java.util.Optional;

public interface DiaryRepository {
    Diary save(Diary diary);
    String updateDiary(Diary diary);
    String deleteDiary(Integer id);
    Optional<Diary>  findByDate(Integer id , String date);
    List<DiaryList> findAll(Integer id);
    DiaryDetail findDiary(Integer id);
}
