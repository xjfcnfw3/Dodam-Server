package com.example.dodam.dto;

import com.example.dodam.domain.inquiry.Inquiry;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InquiryDto {

    private String title;
    private String content;
    private String answer;
    private String category;
    private String imgPath;
    /*
    단일 문의사항 페이지의 상세 문의사항 출력시 사용되는 Dto
    각 문의사항의 제목과 내용, 답변, 카테고리, 이미지경로 출력
     */
    public static InquiryDto inquiryDto(Inquiry inquiry) {
        return new InquiryDto(
                inquiry.getTitle(),
                inquiry.getContent(),
                inquiry.getAnswer(),
                inquiry.getCategory(),
                inquiry.getImgPath());
    }
}
