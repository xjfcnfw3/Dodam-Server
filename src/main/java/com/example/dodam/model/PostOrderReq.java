package com.example.dodam.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostOrderReq {

    private  int userId;
    private  String address;
    private  String babyName;
    private  String diaryTitle;
    private  String startDate;
    private  String endDate;
    private  int templateNo;
    private  String createTime;
    private  String updateTime;
    private  String isDeleted;
    private  String deletedTime;
}

