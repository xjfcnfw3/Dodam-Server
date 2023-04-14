package com.example.dodam.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class MainStepResponse {
    private String memberNickName;
    private int dDay;
    private List<StepResponse> restStep;
    private List<StepResponse> allStep;
}
