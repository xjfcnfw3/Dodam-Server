package com.example.dodam.controller.diary;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.dodam.config.auth.MemberDetails;
import com.example.dodam.domain.diary.Diary;
import com.example.dodam.domain.diary.dto.DiaryRequest;
import com.example.dodam.security.WithCustomMember;
import com.example.dodam.service.diary.DiaryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(DiaryController.class)
class DiaryControllerTest {

    @MockBean
    private DiaryService diaryService;

    private MockMvc mvc;
    private Diary diary;
    private ObjectMapper objectMapper;

    @BeforeEach
    void init(WebApplicationContext context) {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity())
            .build();

        diary = Diary.builder()
            .id(1L)
            .title("This is diary")
            .content("diary")
            .talkToBaby("hello")
            .content("content")
            .feel("Good")
            .imgPath("/test/hello.jpg")
            .date(LocalDate.now())
            .build();

        objectMapper = new ObjectMapper();
    }

    @DisplayName("로그인된 유저의 다이어리 리스트가 출력된다.")
    @Test
    @WithCustomMember
    void getDiaryList() throws Exception {
        Diary diary1 = Diary.builder().id(1L).title("hello1").feel("Good").date(LocalDate.now()).build();
        Diary diary2 = Diary.builder().id(2L).title("hello2").feel("Bad").date(LocalDate.now()).build();
        MemberDetails details = (MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        details.getMember().addDiary(diary1);
        details.getMember().addDiary(diary2);

        mvc.perform(get("/diary"))
            .andExpect(status().isOk())
            .andDo(print())
            .andExpect(jsonPath("$[0]").exists())
            .andExpect(jsonPath("$[1]").exists())
            .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @DisplayName("다이어리를 조회한다.")
    @Test
    @WithCustomMember
    void getDiaryDetail() throws Exception {
        given(diaryService.findDiary(any())).willReturn(diary);
        mvc.perform(get("/diary/1"))
            .andExpect(status().isOk())
            .andDo(print());
        verify(diaryService).findDiary(any());
    }

    @DisplayName("다이어리를 추가한다.")
    @Test
    @WithCustomMember
    void addDiary() throws Exception {
        given(diaryService.addDiary(any(), any())).willReturn(diary);
        MockMultipartFile image =
            new MockMultipartFile("test", "test.jpg", "image/jpg", "imag".getBytes());

        mvc.perform(multipart("/diary")
                .file(image)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .param("content", "diary")
                .param("feel", "Good")
                .param("title", "This is diary")
                .param("talkToBaby", "hello")
                .param("date", LocalDate.now().toString())
                .with(csrf()))
            .andExpect(status().isOk())
            .andDo(print());
        verify(diaryService).addDiary(any(), any());
    }

    @DisplayName("다이어리를 업데이트 한다.")
    @WithCustomMember
    @Test
    void updateDiary() throws Exception {
        DiaryRequest request =
            DiaryRequest.builder().feel("Bad").content("test").date(LocalDate.of(2022, 2, 2)).build();

        mvc.perform(patch("/diary/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.registerModule(new JavaTimeModule()).writeValueAsString(request))
                .with(csrf()))
            .andExpect(status().isOk())
            .andDo(print());

        verify(diaryService).updateDiaryContent(any(), any(), any());
    }

    @DisplayName("다이어리를 삭제한다.")
    @Test
    @WithCustomMember
    void deleteDiary() throws Exception {
        mvc.perform(delete("/diary/1")
                .with(csrf()))
            .andExpect(status().isOk())
            .andDo(print());

        verify(diaryService).deleteDiary(any(), any());
    }

    @DisplayName("다이어리 이미지를 삭제한다.")
    @Test
    @WithCustomMember
    void deleteDiaryImage() throws Exception {
        mvc.perform(delete("/diary/image/1")
                .with(csrf()))
            .andExpect(status().isOk())
            .andDo(print());

        verify(diaryService).deleteDiaryImage(any(), any());
    }
}
