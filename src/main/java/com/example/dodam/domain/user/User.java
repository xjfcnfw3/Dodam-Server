package com.example.dodam.domain.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String email;
    private String password;
    private String phone;
    private String nickname;
    private String status;
    private String imgPath;
    private String role;
    private LocalDate birthDate;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;
    private LocalDateTime startDate;
}
