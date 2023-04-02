package com.example.dodam.domain.member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member {
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

    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    private LocalDateTime startDate;

    @PrePersist
    private void initMember() {
        this.createAt = LocalDateTime.now();
    }
}
