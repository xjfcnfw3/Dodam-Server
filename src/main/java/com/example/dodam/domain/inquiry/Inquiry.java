package com.example.dodam.domain.inquiry;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inquiry {
    private Long id;
    private Long userId;
    private String title;
    private String content;
    private String answer;
    private boolean status;
    private String category;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private String imgPath;
    private String fileName;
}
