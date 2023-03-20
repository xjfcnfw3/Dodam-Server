package com.example.dodam.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "step")
public class Step {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int stepId;

    @Column(nullable = false)
    private Long userId;

    @Column
    private String stepName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private int stepOrder;


    public void changeOrder(int newOrder){
        this.stepOrder = newOrder;
    }

    public void changeStep(String name, LocalDate startDate, LocalDate endDate){
        this.stepName = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

}
