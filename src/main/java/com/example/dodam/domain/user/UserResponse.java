package com.example.dodam.domain.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String email;
    private String phone;
    private String nickname;
    private LocalDateTime startDate;
    private LocalDate birthDate;
    private String imgPath;

    public static UserResponse of(User user) {
        return UserResponse.builder()
            .email(user.getEmail())
            .phone(user.getPhone())
            .nickname(user.getNickname())
            .startDate(user.getStartDate())
            .birthDate(user.getBirthDate())
            .imgPath(user.getImgPath())
            .build();
    }
}
