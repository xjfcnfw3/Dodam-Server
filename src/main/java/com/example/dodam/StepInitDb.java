package com.example.dodam;

import com.example.dodam.dto.StepAddDto;
import com.example.dodam.service.StepService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class StepInitDb {   //샘플db
    private final InitService initService;

    @PostConstruct
    public void init() {
//        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;
        private final StepService stepService;
        public void dbInit1() {
            StepAddDto dto = StepAddDto.builder()
                    .stepName("first")
                    .startDate(LocalDate.of(2023,01,01))
                    .endDate(LocalDate.now())
                    .build();

            stepService.addStep(0L,dto);
            stepService.addStep(0L,dto);
            stepService.addStep(1L,dto);
            stepService.addStep(0L,dto);

        }
    }

}
