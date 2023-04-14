package com.example.dodam.service;

import com.example.dodam.domain.member.Member;
import com.example.dodam.dto.StepEnrollResponse;
import com.example.dodam.dto.MainStepResponse;
import com.example.dodam.domain.Step;
import com.example.dodam.dto.StepRequest;
import com.example.dodam.dto.StepResponse;
import com.example.dodam.repository.StepRepository;
import com.example.dodam.repository.member.MemberRepository;
import java.util.NoSuchElementException;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StepService {

    private final StepRepository stepRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MainStepResponse getMainStep(Member member) {
        int dDay = getDecimalDay(member);
        List<Step> restSteps = getRestStep(member);
        return MainStepResponse.builder()
                .allStep(StepResponse.listOf(member.getSteps()))
                .restStep(StepResponse.listOf(restSteps))
                .dDay(dDay)
                .memberNickName(member.getNickname()).build();
    }

    @Transactional(readOnly = true)
    public StepEnrollResponse getStepEnroll(Member member) {
        return StepEnrollResponse.builder()
            .startDate(member.getStartDate())
            .allStep(StepResponse.listOf(member.getSteps()))
            .build();
    }

    public Step getStep(Long stepId) {
        return stepRepository.findById(stepId).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public void updateStep(Long id, StepRequest request) {
        Step step = stepRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        step.changeOrder(request.toStep().getStepOrder());
        step.update(request.toStep());
    }

    @Transactional
    public void delete(Long stepId) {
        Step step = stepRepository.findById(stepId).orElseThrow(EntityNotFoundException::new);
        stepRepository.delete(step);
    }

    @Transactional
    public Step addStep(Long userId, StepRequest request) {
        Member member = memberRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        Step step = request.toStep();
        setStartDate(step, member);
        return stepRepository.save(step);
    }

    private void setStartDate(Step step, Member member) {
        member.setStartDate(getNearestDate(step.getStartDate(), member.getStartDate()));
    }

    private LocalDate getNearestDate(LocalDate stepDate, LocalDate memberDate) {
        if (memberDate == null) {
            return stepDate;
        } else if (stepDate.isAfter(memberDate)) {
            return memberDate;
        }
        return stepDate;
    }

    private List<Step> getRestStep(Member member) {
        List<Step> restStep = new ArrayList<>();
        for (Step step : member.getSteps()) {
            if (step.getStartDate().compareTo(LocalDate.now()) <= 0 && step.getEndDate().compareTo(LocalDate.now()) >= 0)
                restStep.add(step);
        }
        return restStep;
    }

    private int getDecimalDay(Member member) {
        if(member.getStartDate()!= null)
            return (int) ChronoUnit.DAYS.between(LocalDate.from(member.getStartDate()), LocalDate.now());
        return 0;
    }
}
