package com.example.dodam.domain.diary;

import com.example.dodam.domain.common.BaseTimeEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Diary extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public Long userId;
    public Date date;
    public String title;
    public String imgPath;
    public String talkToBaby;
    public String feel;
    public String content;

    public void updateImagePath(String imgPath) {
        this.imgPath = imgPath;
    }

    public void updateDiaryContent(Diary diary) {
        if (diary.getDate() != null) {
            this.date = diary.getDate();
        }

        if (diary.getContent() != null) {
            this.content = diary.getContent();
        }

        if (diary.getTalkToBaby() != null) {
            this.talkToBaby = diary.getTalkToBaby();
        }

        if (diary.getTitle() != null) {
            this.title = diary.getTitle();
        }

        if (diary.getFeel() != null) {
            this.feel = diary.getFeel();
        }
    }
}
