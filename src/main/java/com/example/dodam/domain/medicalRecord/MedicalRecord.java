package com.example.dodam.domain.medicalRecord;

import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
public class MedicalRecord {
    public Integer id;
    public Integer userId;
    public Date date;
    public String detail;
}
