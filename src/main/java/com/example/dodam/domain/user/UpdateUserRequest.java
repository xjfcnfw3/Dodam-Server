package com.example.dodam.domain.user;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String password;
    private String phone;
    private String nickname;
    private String status;
    private String imgPath;
    private String role;
    private MultipartFile profileImage;
    private LocalDate birthDate;
    private LocalDateTime updateAt;
    private LocalDateTime startDate;

    public User toUser() {
        return User.builder()
            .password(password)
            .phone(phone)
            .nickname(nickname)
            .status(status)
            .imgPath(imgPath)
            .role(role)
            .birthDate(birthDate)
            .updateAt(updateAt)
            .startDate(startDate)
            .build();
    }
}
