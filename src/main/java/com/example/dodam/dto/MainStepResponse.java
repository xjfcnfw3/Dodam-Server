package com.example.dodam.dto;

import com.example.dodam.domain.Step;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Builder
@ToString
@Getter
public class MainStepResponse {
    private String memberNickName;
    private int dDay;
    private List<Step> restStep;
    private List<Step> allStep;
}
