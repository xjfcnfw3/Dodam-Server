package com.example.dodam.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DeleteOrderReq {

        //where 절을 적용할 변수
        private int orderId;

        //수정할 변수들
        private  String isDeleted;    //삭제 여부



 }

