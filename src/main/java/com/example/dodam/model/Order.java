package com.example.dodam.model;


import lombok.*;

@NoArgsConstructor
@Getter // 해당 클래스에 대한 접근자 생성
@Setter // 해당 클래스에 대한 설정자 생성
@AllArgsConstructor
//@AllArgsConstructor // 해당 클래스의 모든 멤버 변수(userIdx, nickname, email, password)를 받는 생성자를 생성
/**
 * 주문과 관련된 정보들을 담고 있는 클래스(주문 관련정보를 추출할 때 해당 클래스에서 Getter를 사용해서 가져온다.)
 * GetOrderRes는 클라이언트한테 response줄 때 DTO고(DTO란 DB의 정보를 Service나 Controller등에 보낼때 사용하는 객체를 의미한다.)
 * Order 클래스는 스프링에서 사용하는 Object이다.(내부에서 사용하기 위한 객체라고 보면 된다.)
 */
public class Order {
    //수정에 필요한 모든 변수들
    private int orderId;  //주문아이디(자동생성됨)   //없어도 될 것 같음
    private int userId;        //유저아이디
    private  String address;    //주소
    private  String babyName;    //아기이름
    private  String diaryTitle;   //다이어리 제목
    private  String startDate;    //시작일시
    private  String endDate;      //종료일시
    private  int templateNo;      //템플릿번호
    private  String createTime;   //주문생성시간
    private  String updateTime;   //주문업데이트시간
    public   String isDeleted;    //주문삭제여부
    private  String deletedTime;   //삭제일시
}
