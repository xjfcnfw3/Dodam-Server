package com.example.dodam.domain.member;

import com.example.dodam.domain.common.BaseTimeEntity;
import com.example.dodam.domain.diary.Diary;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email(message = "Email should be valid")
    private String email;

    @Size(min = 8, max = 100, message = "password must be between 8 and 100 characters")
    private String password;

    private String phone;

    @NotBlank
    private String nickname;

    private String status;

    private String imgPath;

    private String role;

    private LocalDate birthDate;

    private LocalDateTime startDate;

    @Builder.Default
    @OrderBy("date")
    @OneToMany(orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "member")
    private List<Diary> diaries = new ArrayList<>();

    public void update(Member member) {
        if (member.password != null) {
            this.password = member.password;
        }

        if (member.nickname != null) {
            this.nickname = member.nickname;
        }

        if (member.startDate != null) {
            this.startDate = member.startDate;
        }
    }

    public void addDiary(Diary diary) {
        this.diaries.add(diary);
    }

    public void deleteDiary(Diary diary) {
        this.diaries.remove(diary);
    }
}
