package com.example.dodam.domain.schedule;

import lombok.Getter;
import lombok.Setter;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
public class Schedule {
    public Integer scheduleId;
    public Integer userId;
    public String name;
    public String repeatStatus;
    public String selectDate;
    public Integer selectDay;
    public Date startDate;
    public Date endDate;
    public Time startTime;
    public Time endTime;
    public String color;

}
