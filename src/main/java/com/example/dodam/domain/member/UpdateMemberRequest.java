package com.example.dodam.domain.member;

import java.time.LocalDate;
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
public class UpdateMemberRequest {
    private String password;
    private String nickname;
    private String imgPath;
    private MultipartFile profileImage;
    private LocalDate birthDate;
    private LocalDate startDate;

    public Member toUser() {
        return Member.builder()
            .password(password)
            .nickname(nickname)
            .imgPath(imgPath)
            .birthDate(birthDate)
            .startDate(startDate)
            .build();
    }
}
