package com.example.dodam.domain.user;

import java.time.LocalDate;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {

    @Email
    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String nickname;

    @NotBlank
    private String phone;
    private LocalDate birthDate;
    private MultipartFile profileImage;

    private String imagePath;

    public User toUser() {
        return User.builder()
            .email(email)
            .password(password)
            .phone(phone)
            .nickname(nickname)
            .birthDate(birthDate)
            .imgPath(imagePath)
            .build();
    }
}
