package com.example.dodam.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.dodam.domain.member.Member;
import com.example.dodam.domain.Step;
import com.example.dodam.dto.MainStepResponse;
import com.example.dodam.dto.StepEnrollResponse;
import com.example.dodam.dto.StepRequest;
import com.example.dodam.repository.StepRepository;
import com.example.dodam.repository.member.MemberRepository;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class StepServiceTest {

    private StepService stepService;

    @Mock
    private StepRepository stepRepository;

    @Mock
    private MemberRepository memberRepository;

    private Step step;
    private Member member;

    @BeforeEach
    void init() {
        stepService = new StepService(stepRepository, memberRepository);

        member = Member.builder()
            .id(1L)
            .email("test@naver.com")
            .password("12345667")
            .nickname("test")
            .build();

        step = Step.builder()
            .stepId(1L)
            .stepName("first")
            .startDate(LocalDate.of(2023,1,1))
            .endDate(LocalDate.now())
            .stepOrder(0)
            .build();
    }

    @DisplayName("단계등록 Test")
    @Test
    void saveStepTest(){
        StepRequest request = StepRequest.builder()
            .stepName("first")
            .startDate(LocalDate.of(2023,1,1))
            .stepOrder(0)
            .endDate(LocalDate.now())
            .build();
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        stepService.addStep(member.getId(), request);

        assertAll(
            () -> assertThat(member.getStartDate()).isEqualTo(request.getStartDate()),
            () -> verify(memberRepository).findById(any())
        );
    }

    @DisplayName("단계 저장시 가장 이전의 날짜가 저장된다.")
    @MethodSource("getStartDateArguments")
    @ParameterizedTest
    void setStartDateTest(StepRequest request, LocalDate answerDate) {
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));
        member.setStartDate(LocalDate.now());

        stepService.addStep(member.getId(), request);

        assertAll(
            () -> assertThat(member.getStartDate()).isEqualTo(answerDate),
            () -> verify(memberRepository).findById(any())
        );
    }

    @Test
    @DisplayName("단계삭제 Test")
    void deleteTest(){
        when(stepRepository.findById(any())).thenReturn(Optional.of(step));

        stepService.delete(1L);

        assertAll(
            () -> verify(stepRepository).findById(any()),
            () -> verify(stepRepository).delete(any())
        );
    }

    @Test
    @DisplayName("단계 수정 테스트")
    void updateStepTest(){
        StepRequest request = StepRequest.builder()
            .stepName("변경")
            .stepOrder(1)
            .startDate(LocalDate.now())
            .endDate(LocalDate.MAX)
            .build();

        when(stepRepository.findById(any())).thenReturn(Optional.of(step));

        stepService.updateStep(1L, request);

        assertAll(
            () -> assertThat(step.getStepName()).isEqualTo(request.getStepName()),
            () -> assertThat(step.getStepOrder()).isEqualTo(request.getStepOrder()),
            () -> assertThat(step.getStartDate()).isEqualTo(request.getStartDate()),
            () -> assertThat(step.getEndDate()).isEqualTo(request.getEndDate()),
            () -> verify(stepRepository).findById(any())
        );
    }

    @Test
    @DisplayName("단계 조회 테스트")
    void getStep() {
        when(stepRepository.findById(any())).thenReturn(Optional.of(step));

        stepService.getStep(1L);

        verify(stepRepository).findById(any());
    }

    @Test
    @DisplayName("한 유저의 전체 과정 단계 조회")
    void getStepEnrollTest(){
        Step step1 = Step.builder()
            .stepId(1L).stepOrder(0).stepName("test1").startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(1)).build();
        Step step2 = Step.builder().stepId(2L).stepOrder(1)
            .stepName("test2").startDate(LocalDate.now().plusDays(2))
            .endDate(LocalDate.now().plusDays(3)).build();
        member.addStep(step1);
        member.addStep(step2);
        member.setStartDate(step1.getStartDate());

        StepEnrollResponse stepEnroll = stepService.getStepEnroll(member);

        assertAll(
            () -> assertThat(stepEnroll.getAllStep()).hasSize(2),
            () -> assertThat(stepEnroll.getStartDate()).isEqualTo(member.getStartDate())
        );
    }

    @DisplayName("사용자의 단계에 대한 전체젝인 정보를 조회")
    @Test
    void getMainStepTest(){
        Step step1 = Step.builder().stepId(1L).stepOrder(1)
            .stepName("test2").startDate(LocalDate.now().minusDays(1))
            .endDate(LocalDate.now().plusDays(3)).build();
        Step step2 = Step.builder().stepId(2L).stepOrder(0).stepName("test1")
            .startDate(LocalDate.now().plusDays(10))
            .endDate(LocalDate.now().plusDays(11)).build();
        member.addStep(step1);
        member.addStep(step2);
        member.setStartDate(step1.getStartDate());

        MainStepResponse response = stepService.getMainStep(member);

        assertAll(
            () -> assertThat(response.getAllStep()).hasSize(2),
            () -> assertThat(response.getRestStep()).hasSize(1),
            () -> assertThat(response.getDDay()).isEqualTo(1)
        );
    }

    @DisplayName("사용자의 단계가 없으면 D-day는 0을 출력")
    @Test
    void getMainStep() {
        MainStepResponse response = stepService.getMainStep(member);

        assertAll(
            () -> assertThat(response.getAllStep()).isEmpty(),
            () -> assertThat(response.getRestStep()).isEmpty(),
            () -> assertThat(response.getDDay()).isZero()
        );
    }

    private static Stream<Arguments> getStartDateArguments() {
        return Stream.of(
            Arguments.arguments(
                StepRequest.builder().startDate(LocalDate.now().minusDays(1)).build(),
                LocalDate.now().minusDays(1)
            ),
            Arguments.arguments(
                StepRequest.builder().startDate(LocalDate.now().plusDays(1)).build(),
                LocalDate.now()
            )
        );
    }
}
