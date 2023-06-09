
package com.example.dodam.domain.diary.dto;

import com.example.dodam.domain.diary.Diary;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DiaryRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    private String title;
    private MultipartFile diaryImage;
    private String talkToBaby;
    private String feel;
    private String content;

    public Diary toDiary() {
        return Diary.builder()
            .date(date)
            .title(title)
            .talkToBaby(talkToBaby)
            .feel(feel)
            .content(content)
            .build();
    }
}
