package com.example.dodam;

import com.example.dodam.repository.diary.DiaryRepository;
import com.example.dodam.repository.diary.JdbcDiaryRepository;
import com.example.dodam.service.diary.DiaryService;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;

@org.springframework.context.annotation.Configuration
public class Configuration {
    private final DataSource dataSource;

    public Configuration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public DiaryService diaryService(){
        return new DiaryService(diaryRepository());
    }
    @Bean
    public DiaryRepository diaryRepository(){
        return new JdbcDiaryRepository(dataSource);
    }

}
