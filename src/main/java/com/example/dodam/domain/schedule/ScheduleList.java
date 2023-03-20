package com.example.dodam.domain.schedule;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ScheduleList {
    public Integer scheduleId;
    public String name;
    public String repeatStatus;
    public String selectDate;
    public Integer selectDay;
    public Date startDate;
    public Date endDate;
    public String color;
}
