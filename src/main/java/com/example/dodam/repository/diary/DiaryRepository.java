package com.example.dodam.repository.diary;

import com.example.dodam.domain.diary.Diary;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiaryRepository extends JpaRepository<Diary, Long> {

    Optional<Diary> findByDate(String date);
}
