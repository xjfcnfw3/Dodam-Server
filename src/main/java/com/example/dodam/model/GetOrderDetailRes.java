package com.example.dodam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 상세 주문 조회
 */

@Getter
@Setter
@AllArgsConstructor
public class GetOrderDetailRes {

    //모든 변수들
    private int orderDetailId;  //상세주문아이디(자동생성됨)
    private int orderId;        //주문번호
    private  String deliveryService;    //배송회사
    private  String invoiceNo;    //송장번호
    private  String deliveryStatus;  //배송상태




}
