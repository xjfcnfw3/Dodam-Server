package com.example.dodam.service;

import com.example.dodam.domain.member.Member;
import com.example.dodam.dto.StepAddDto;
import com.example.dodam.dto.StepEnrollDto;
import com.example.dodam.dto.StepMainDto;
import com.example.dodam.domain.Step;
import com.example.dodam.dto.StepRequest;
import com.example.dodam.repository.StepRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;


@Disabled
@SpringBootTest
@Transactional
class StepServiceTest {
    @Autowired
    StepService stepService;

    @Autowired
    StepRepository stepRepository;

    public Step createStep(){
        Step step = Step.builder()
                .stepName("first")
                .startDate(LocalDate.of(2023,01,01))
                .endDate(LocalDate.now())
                .stepOrder(0).build();
        return step;
    }

    public void createEx(){
        StepAddDto dto = StepAddDto.builder()
                .stepName("first")
                .startDate(LocalDate.of(2023,01,01))
                .endDate(LocalDate.now())
                .build();

        stepService.addStep(0L,dto);
        stepService.addStep(0L,dto);
        stepService.addStep(1L,dto);
        stepService.addStep(0L,dto);
        System.out.println("stepRepository.findAllByUserId(0) = " + stepRepository.findAllByMemberId(0L));
    }

    @Test
    @DisplayName("단계등록 Test")
    public void SaveStepTest(){
        //given
        StepAddDto dto = StepAddDto.builder()
                .stepName("first")
                .startDate(LocalDate.of(2023,01,01))
                .endDate(LocalDate.now())
                .build();
        //when
        stepService.addStep(0L,dto);
        stepService.addStep(0L,dto);
        stepService.addStep(1L,dto);
        stepService.addStep(0L,dto);
        //then
        //저장확인
        //순서확인
        Assertions.assertThat(stepRepository.findAllByMemberId(0L).get(2).getStepOrder()).isEqualTo(2);
        System.out.println("stepRepository.findAllByUserId(0) = " + stepRepository.findAllByMemberId(0L));
        System.out.println("stepRepository = " + stepRepository.findAll());
    }

    @Test
    @DisplayName("단계삭제 Test")
    public void deleteTest(){
        //given
        createEx();

        //when

        //then
        System.out.println("stepRepository.findAllByUserId(0) = " + stepRepository.findAllByMemberId(0L));
        Assertions.assertThat(stepRepository.findAllByMemberId(0L).get(1).getStepOrder()).isEqualTo(1);

    }

    @Test
    @DisplayName("단계 변경Test")
    public void changeStepTest(){
        //given

        //then 
    }

    @Test
    @DisplayName("순서 변경 Test")
    public void changeOrder(){
        //given
        createEx();
        StepRequest dto = StepRequest.builder()
                .stepName("second")
                .startDate(LocalDate.of(2002,07,15))
                .endDate(LocalDate.now()).build();

        //when
        Step stepOrigin0 = stepRepository.findByStepOrderAndMemberId(0,0L);
        Step stepOrigin1 = stepRepository.findByStepOrderAndMemberId(1,0L);
        stepService.changeOrder(0L,0,1);

        //then
        Assertions.assertThat(stepOrigin0.getStepOrder()).isEqualTo(1);
        Assertions.assertThat(stepOrigin1.getStepOrder()).isEqualTo(0);
        System.out.println("stepRepository = " + stepRepository.findAll());
    }

    @Test
    @DisplayName("단계등록Get Test")
    public void getStepEnrollTest(Member member){
        //given
        createEx();

        //when
        StepEnrollDto stepEnrollDto =  stepService.getStepEnroll(member);

        //then
        System.out.println("stepEnrollDto.toString() = " + stepEnrollDto.toString());
    }

    @Test
    public void getMainStepTest(Member member){
        //given
        createEx();

        //when
        StepMainDto stepMainDto = stepService.getMainStep(member);
        
        //then
        System.out.println("stepRepository.findAll() = " + stepRepository.findAll());
        System.out.println("stepMainDto.toString() = " + stepMainDto.toString());
    }
}
