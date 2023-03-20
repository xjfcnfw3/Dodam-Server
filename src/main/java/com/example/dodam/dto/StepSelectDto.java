package com.example.dodam.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class StepSelectDto {
        private int stepId;
        private String stepName;
        private LocalDate startDate;
        private LocalDate endDate;

}
