package com.example.dodam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GetOrderRes {

    //모든 변수들
    private int orderId;  //주문아이디(자동생성됨)
    private int userId;        //유저아이디
    private  String address;    //주소
    private  String babyName;    //아기이름
    private  String diaryTitle;   //다이어리 제목
    private  String startDate;    //시작일시
    private  String endDate;      //종료일시
    private  int templateNo;      //템플릿번호
    private  String createTime;   //주문생성시간
    private  String updateTime;   //주문업데이트시간
    private  String isDeleted;    //주문삭제여부
    private  String deletedTime;   //삭제일시
}

