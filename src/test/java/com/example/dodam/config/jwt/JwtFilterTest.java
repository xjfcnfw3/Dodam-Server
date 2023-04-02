package com.example.dodam.config.jwt;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dodam.common.exception.EntityNotFoundException;
import com.example.dodam.config.CorsConfig;
import com.example.dodam.config.SecurityConfig;
import com.example.dodam.config.auth.MemberDetails;
import com.example.dodam.config.auth.MemberDetailsService;
import com.example.dodam.controller.user.AuthController;
import com.example.dodam.domain.member.Member;
import com.example.dodam.security.TokenProvider;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@WebAppConfiguration
@Import({SecurityConfig.class, CorsConfig.class})
@WebMvcTest(controllers = AuthController.class)
@ExtendWith(SpringExtension.class)
class JwtFilterTest {

    private static final String EMAIL = "hello@naver.com";
    private static final String PASSWORD = "1234";

    @MockBean
    private MemberDetailsService memberDetailsService;

    @MockBean
    private TokenProvider tokenProvider;

    private MockMvc mvc;
    private String accessToken;
    private MemberDetails memberDetails;

    @BeforeEach
    void init(WebApplicationContext context) {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        accessToken = Jwts.builder()
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 10000L))
            .setSubject(EMAIL)
            .compact();

        Member member = Member.builder()
            .id(1L)
            .email(EMAIL)
            .password(PASSWORD)
            .role("USER")
            .build();
        memberDetails = new MemberDetails(member);
    }

    @Test
    void access() throws Exception {
        given(memberDetailsService.loadUserByUsername(any(String.class))).willReturn(memberDetails);
        given(tokenProvider.validateAccessToken(any(String.class))).willReturn(true);
        given(tokenProvider.getAttribute(any(String.class))).willReturn(EMAIL);
        mvc.perform(get("/test/auth")
                .header(JwtFilter.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void defined() throws Exception {
        given(tokenProvider.validateAccessToken(any(String.class))).willReturn(false);
        mvc.perform(get("/test/auth")
                .header(JwtFilter.AUTHORIZATION, "not token")
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf()))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void inputBlankToken() throws Exception {
        mvc.perform(get("/test/auth")
                .header(JwtFilter.AUTHORIZATION, "")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }

    @Test
    void isNotMember() throws Exception {
        given(tokenProvider.validateAccessToken(any(String.class))).willReturn(true);
        given(tokenProvider.getAttribute(any(String.class))).willReturn(EMAIL);
        given(memberDetailsService.loadUserByUsername(any(String.class))).willThrow(EntityNotFoundException.class);
        mvc.perform(get("/test/auth")
                .header(JwtFilter.AUTHORIZATION, accessToken)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().is4xxClientError());
    }
}
