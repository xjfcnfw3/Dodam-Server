package com.example.dodam.dto;

import com.example.dodam.domain.Step;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class StepResponse {

    private Long stepId;
    private String stepName;
    private LocalDate startDate;
    private LocalDate endDate;

    public static List<StepResponse> listOf(List<Step> steps) {
        return steps.stream()
            .map(StepResponse::of)
            .collect(Collectors.toList());
    }

    public static StepResponse of(Step step) {
        return StepResponse.builder()
            .stepId(step.getStepId())
            .stepName(step.getStepName())
            .startDate(step.getStartDate())
            .endDate(step.getEndDate())
            .build();
    }
}
