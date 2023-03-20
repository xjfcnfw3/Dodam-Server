package com.example.dodam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * orderId에 따라 상세주문 생성
 */
@Getter
@Setter
@AllArgsConstructor
//@NoArgsConstructor
public class PostOrderDetailRes {
    private String result;  //상세 주문 등록 결과 메시지

}

