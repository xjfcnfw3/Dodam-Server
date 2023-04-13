package com.example.dodam.domain.member;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDate;
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
public class MemberResponse {

    private String email;
    private String phone;
    private String nickname;
    private LocalDate startDate;
    private LocalDate birthDate;
    private String imgPath;

    public static MemberResponse of(Member member) {
        return MemberResponse.builder()
            .email(member.getEmail())
            .phone(member.getPhone())
            .nickname(member.getNickname())
            .startDate(member.getStartDate())
            .birthDate(member.getBirthDate())
            .imgPath(member.getImgPath())
            .build();
    }
}
