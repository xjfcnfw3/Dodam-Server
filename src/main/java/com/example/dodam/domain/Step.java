package com.example.dodam.domain;

import com.example.dodam.domain.common.BaseTimeEntity;
import com.example.dodam.domain.member.Member;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Step extends BaseTimeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

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

    public void update(Step step) {
        if (step.getStepName() != null) {
            this.stepName = step.getStepName();
        }
        if (step.getStartDate() != null) {
           this.startDate = step.getStartDate();
        }
        if (step.getEndDate() != null) {
            this.endDate = step.getEndDate();
        }
    }

    public void associateMember(Member member) {
        this.member = member;
        member.addStep(this);
    }
}
