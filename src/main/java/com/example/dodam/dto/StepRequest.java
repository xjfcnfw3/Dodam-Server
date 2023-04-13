package com.example.dodam.dto;

import com.example.dodam.domain.Step;
import java.time.LocalDate;
import javax.persistence.Column;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StepRequest {
    private String stepName;
    private LocalDate startDate;
    private LocalDate endDate;

    public Step toStep() {
        return Step.builder()
            .stepName(stepName)
            .startDate(startDate)
            .endDate(endDate)
            .build();
    }
}
