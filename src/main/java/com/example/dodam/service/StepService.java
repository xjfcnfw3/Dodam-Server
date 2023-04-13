package com.example.dodam.service;

import com.example.dodam.domain.member.Member;
import com.example.dodam.dto.StepAddDto;
import com.example.dodam.dto.StepEnrollDto;
import com.example.dodam.dto.StepMainDto;
import com.example.dodam.domain.Step;
import com.example.dodam.dto.StepRequest;
import com.example.dodam.repository.StepRepository;
import com.example.dodam.repository.member.MemberRepository;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
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

    public StepMainDto getMainStep(Member member) {
        LocalDate now = LocalDate.now();
        int dDay;
        if(member.getStartDate()!= null)
            dDay= (int) ChronoUnit.DAYS.between(LocalDate.from(member.getStartDate()),now);
        else
            dDay = 0;

        List<Step> stepAll = stepRepository.findAllByMemberId(member.getId());
        List<String> nowStep = new ArrayList<>();

        for (Step step : stepAll) {
            if (step.getStartDate().compareTo(now) <= 0 && step.getEndDate().compareTo(now) >= 0)
                nowStep.add(step.getStepName());
        }

        return StepMainDto.builder()
                .allStep(stepAll)
                .nowStep(nowStep)
                .dDay(dDay)
                .memberNickName(member.getNickname()).build();

    }

    public StepEnrollDto getStepEnroll(Member member) {
        LocalDate startDate = null;
        if(member.getStartDate()!= null)
            startDate = LocalDate.from(member.getStartDate());
        List<Step> stepAll = stepRepository.findAllByMemberId(member.getId());

        return StepEnrollDto.builder()
                .startDate(startDate)
                .allStep(stepAll)
                .build();
    }

    public void changeOrder(Long userId, int firstOrder, int secondOrder) {
        Step step1 = stepRepository.findByStepOrderAndMemberId(firstOrder,userId);
        Step step2 = stepRepository.findByStepOrderAndMemberId(secondOrder,userId);

        step1.changeOrder(secondOrder);
        step2.changeOrder(firstOrder);
    }

    public Step getStep(Long stepId) {
        return stepRepository.findById(stepId).orElseThrow(EntityNotFoundException::new);
    }

    public void changeStep(Long id, StepRequest request) {
        Step step = stepRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        step.update(request.toStep());
    }

    public void delete(Long stepId) {
        Step step = stepRepository.findById(stepId).orElseThrow(EntityNotFoundException::new);
        int stepOrder = step.getStepOrder();
        stepRepository.delete(step);
        stepRepository.updateOrder(stepOrder);
    }

    public Long addStep(Long userId, StepAddDto dto) {
        Long order = stepRepository.countStepByMemberId(userId);
        Member member = memberRepository.findById(userId).orElseThrow(NoSuchElementException::new);
        if (member.getStartDate() == null) {
            member.setStartDate(LocalDateTime.now());
        }

        Step step = Step.builder()
                .stepName(dto.getStepName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .stepOrder(Math.toIntExact(order)).build();
        stepRepository.save(step);
        return step.getStepId();
    }

    @Transactional
    public void setStartDate(LocalDateTime startDate, Long userId) {
        Optional<Member> foundMember = memberRepository.findById(userId);
        if (foundMember.isPresent()) {
            Member member = foundMember.get();
            member.setStartDate(startDate);
            memberRepository.save(member);
        }

    }
}
