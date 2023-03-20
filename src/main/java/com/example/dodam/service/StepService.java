package com.example.dodam.service;

import com.example.dodam.domain.user.User;
import com.example.dodam.dto.StepAddDto;
import com.example.dodam.dto.StepEnrollDto;
import com.example.dodam.dto.StepMainDto;
import com.example.dodam.dto.StepSelectDto;
import com.example.dodam.domain.Step;
import com.example.dodam.repository.StepRepository;
import com.example.dodam.repository.user.UserRepository;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.NoSuchElementException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StepService {

    private final StepRepository stepRepository;
    private final UserRepository userRepository;

    public StepMainDto getMainStep(User user) {
        LocalDate now = LocalDate.now();
        int dDay;
        if(user.getStartDate()!= null)
            dDay= (int) ChronoUnit.DAYS.between(LocalDate.from(user.getStartDate()),now);
        else
            dDay = 0;

        List<Step> stepAll = stepRepository.findAllByUserId(user.getId());
        List<String> nowStep = new ArrayList<>();

        for (Step step : stepAll) {
            if (step.getStartDate().compareTo(now) <= 0 && step.getEndDate().compareTo(now) >= 0)
                nowStep.add(step.getStepName());
        }

        return StepMainDto.builder()
                .allStep(stepAll)
                .nowStep(nowStep)
                .dDay(dDay)
                .memberNickName(user.getNickname()).build();

    }

    public StepEnrollDto getStepEnroll(User user) {
        LocalDate startDate = null;
        if(user.getStartDate()!= null)
            startDate = LocalDate.from(user.getStartDate());
        List<Step> stepAll = stepRepository.findAllByUserId(user.getId());

        return StepEnrollDto.builder()
                .startDate(startDate)
                .allStep(stepAll)
                .build();
    }

    public void changeOrder(Long userId, int firstOrder, int secondOrder) {
        Step step1 = stepRepository.findByStepOrderAndUserId(firstOrder,userId);
        Step step2 = stepRepository.findByStepOrderAndUserId(secondOrder,userId);

        step1.changeOrder(secondOrder);
        step2.changeOrder(firstOrder);
    }

    public Step getStep(int stepId) {
        return stepRepository.findByStepId(stepId);
    }

    public void changeStep(StepSelectDto dto) {
        Step step = stepRepository.findByStepId(dto.getStepId());
        step.changeStep(dto.getStepName(),dto.getStartDate(),dto.getEndDate());
    }

    public void delete(int stepId) {
        Step step = stepRepository.findByStepId(stepId);
        int stepOrder = step.getStepOrder();
        stepRepository.delete(step);
        stepRepository.updateOrder(stepOrder);
    }

    public int addStep(Long userId, StepAddDto dto) {
        Long order = stepRepository.countStepByUserId(userId);
        User user = userRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        if (user.getStartDate() == null) {
            userRepository.update(userId, User.builder().startDate(LocalDateTime.now()).build());
        }
        Step step = Step.builder()
                .userId(userId)
                .stepName(dto.getStepName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .stepOrder(Math.toIntExact(order)).build();
        stepRepository.save(step);
        return step.getStepId();
    }

    public void setStartDate(LocalDateTime startDate, Long userId) {
        userRepository.updateStartDate(userId, startDate);
    }
}
