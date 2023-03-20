package com.example.dodam.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class StepAddDto {
    private String stepName;
    private LocalDate startDate;
    private LocalDate endDate;
}
