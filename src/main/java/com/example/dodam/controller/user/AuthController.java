package com.example.dodam.controller.user;

import com.example.dodam.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {


    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/test/auth")
    public String auth() {
        return "ok";
    }

}
