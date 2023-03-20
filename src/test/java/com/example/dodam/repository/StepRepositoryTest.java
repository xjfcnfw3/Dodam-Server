package com.example.dodam.repository;

import com.example.dodam.domain.Step;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDate;

@SpringBootTest
//@TestPropertySource(locations="classpath:application-test.properties")
class StepRepositoryTest {
    @Autowired
    StepRepository stepRepository;

    @PersistenceContext
    EntityManager em;

    public Step createStep(){
        Step step = Step.builder()
                .userId(0L)
                .stepName("first")
                .startDate(LocalDate.of(2023,01,01))
                .endDate(LocalDate.now())
                .stepOrder(0).build();

        return step;
    }

    @Test
    public void saveStep(){
        Step step = createStep();
        System.out.println("step.toString() = " + step.toString());
        Step savedStep = stepRepository.save(step);
        System.out.println("savedStep.toString() = " + savedStep.toString());
    }
}