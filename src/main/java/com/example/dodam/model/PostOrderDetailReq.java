package com.example.dodam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostOrderDetailReq {

    private int orderId;        //주문번호
    private  String deliveryService;    //배송회사
    private  String invoiceNo;    //송장번호
    private  String deliveryStatus;  //배송상태


}

