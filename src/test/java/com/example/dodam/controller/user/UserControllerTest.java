package com.example.dodam.controller.user;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dodam.common.fileupload.service.ProfileUploadService;
import com.example.dodam.security.WithCustomMember;
import com.example.dodam.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(controllers = UserController.class)
class UserControllerTest {

    @MockBean
    private MemberService memberService;

    @MockBean
    private ProfileUploadService profileUploadService;

    private MockMvc mvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init(WebApplicationContext context) {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        objectMapper = new ObjectMapper();
    }

    @Test
    @WithCustomMember
    void getMember() throws Exception {
        mvc.perform(get("/user"))
            .andExpect(status().isOk())
            .andDo(print());
    }
}
