package com.example.dodam.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dodam.domain.Step;
import com.example.dodam.domain.member.Member;
import com.example.dodam.dto.MainStepResponse;
import com.example.dodam.dto.StepEnrollResponse;
import com.example.dodam.dto.StepRequest;
import com.example.dodam.dto.StepResponse;
import com.example.dodam.security.WithCustomMember;
import com.example.dodam.service.StepService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(StepController.class)
class StepControllerTest {

    @MockBean
    private StepService stepService;

    private MockMvc mvc;
    private Step step1;
    private Step step2;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init(WebApplicationContext context) {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        step1 = Step.builder().stepId(1L).stepOrder(1)
            .stepName("test2").startDate(LocalDate.now().minusDays(1))
            .endDate(LocalDate.now().plusDays(3)).build();
        step2 = Step.builder().stepId(2L).stepOrder(0).stepName("test1")
            .startDate(LocalDate.now().plusDays(10))
            .endDate(LocalDate.now().plusDays(11)).build();

        objectMapper = new ObjectMapper();
    }

    @DisplayName("전체적인 단계와 추가정보를 요청한다.")
    @Test
    @WithCustomMember
    void getMainStep() throws Exception {
        MainStepResponse response = MainStepResponse.builder()
            .allStep(StepResponse.listOf(List.of(step1, step2))).restStep(StepResponse.listOf(List.of(step2)))
            .dDay(22).build();

        given(stepService.getMainStep(any())).willReturn(response);

        mvc.perform(get("/step/main"))
            .andExpect(status().isOk())
            .andDo(print());
        verify(stepService).getMainStep(any());
    }

    @DisplayName("전체 단계를 요청한다.")
    @Test
    @WithCustomMember
    void getStepEnroll() throws Exception {
        StepEnrollResponse response = StepEnrollResponse.builder().allStep(StepResponse.listOf(List.of(step1, step2)))
            .startDate(step1.getStartDate()).build();
        given(stepService.getStepEnroll(any())).willReturn(response);

        mvc.perform(get("/step/enroll"))
            .andExpect(status().isOk())
            .andDo(print());
        verify(stepService).getStepEnroll(any());
    }

    @DisplayName("단계 정보를 요청한다.")
    @Test
    @WithCustomMember
    void getStep() throws Exception {
        given(stepService.getStep(any())).willReturn(step1);

        mvc.perform(get("/step/1"))
            .andExpect(status().isOk())
            .andDo(print());
        verify(stepService).getStep(any());
    }

    @DisplayName("단계 업데이트를 요청한다.")
    @Test
    @WithCustomMember
    void update() throws Exception {
        StepRequest request = StepRequest.builder()
            .startDate(LocalDate.now().minusYears(1))
            .endDate(LocalDate.now().plusDays(1))
            .stepName("변경")
            .build();

        mvc.perform(patch("/step/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(request))
                .with(csrf()))
            .andExpect(status().isOk())
            .andDo(print());
        verify(stepService).updateStep(any(), any());
    }

    @DisplayName("단계 삭제를 요청한다.")
    @Test
    @WithCustomMember
    void delete() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete("/step/1")
                .with(csrf()))
            .andExpect(status().isOk())
            .andDo(print());
        verify(stepService).delete(any());
    }

    @DisplayName("단계 추가를 요청한다.")
    @Test
    @WithCustomMember
    void addStep() throws Exception {
        StepRequest request = StepRequest.builder()
            .stepName(step1.getStepName())
            .endDate(step1.getEndDate())
            .startDate(step1.getStartDate())
            .stepOrder(step1.getStepOrder())
            .build();
        given(stepService.addStep(any(), any())).willReturn(step1);

        mvc.perform(post("/step")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(request))
                .with(csrf()))
            .andExpect(status().isOk())
            .andDo(print());
        verify(stepService).addStep(any(), any());
    }
}
