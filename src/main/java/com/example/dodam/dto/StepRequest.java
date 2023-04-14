package com.example.dodam.dto;

import com.example.dodam.domain.Step;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class StepRequest {
    private String stepName;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer stepOrder;

    public Step toStep() {
        return Step.builder()
            .stepName(stepName)
            .startDate(startDate)
            .endDate(endDate)
            .stepOrder(stepOrder)
            .build();
    }
}
