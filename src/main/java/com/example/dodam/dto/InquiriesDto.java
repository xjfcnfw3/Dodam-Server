package com.example.dodam.dto;

import com.example.dodam.domain.inquiry.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiriesDto {
    private String title;
    private  boolean status;
    private LocalDateTime createAt;

    /*
    첫 문의사항 페이지의 사용자 별 문의사항 리스트 출력시 사용되는 Dto
    각 문의사항의 제목과 작성날짜, 답변유무인 status만을 출력
     */
    public static InquiriesDto inquiriesDto(Inquiry inquiry) {
        return new InquiriesDto(
                inquiry.getTitle(),
                inquiry.isStatus(),
                inquiry.getCreateAt());
    }
}
