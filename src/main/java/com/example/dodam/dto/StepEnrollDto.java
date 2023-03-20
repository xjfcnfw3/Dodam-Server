package com.example.dodam.dto;

import com.example.dodam.domain.Step;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Builder
@ToString
@Getter
public class StepEnrollDto {
    private List<Step> allStep;
    private LocalDate startDate;
}
