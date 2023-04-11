package com.example.dodam.service.diary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.dodam.common.fileupload.service.DiaryImageUploadService;
import com.example.dodam.domain.diary.Diary;
import com.example.dodam.domain.diary.dto.DiaryRequest;
import com.example.dodam.domain.member.Member;
import com.example.dodam.repository.diary.DiaryRepository;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
class DiaryServiceTest {

    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private DiaryImageUploadService uploadService;

    private DiaryService diaryService;
    private DiaryRequest request;
    private Diary diary;
    private Member member;

    @BeforeEach
    void init() {
        diaryService = new DiaryService(diaryRepository, uploadService);

        request = DiaryRequest.builder()
            .diaryImage(new MockMultipartFile("test", "<img>".getBytes()))
            .date(new Date())
            .title("hello")
            .content("test")
            .build();

        diary = Diary.builder()
            .id(1L)
            .title("hello")
            .content("test")
            .date(request.getDate())
            .build();

        member = Member.builder()
            .id(1L)
            .email("test@naver.com")
            .password("12345678")
            .nickname("test")
            .build();
    }

    @Test
    void findDiaries() {
        Diary diary1 = Diary.builder().id(1L).title("hello").content("world").build();
        Diary diary2 = Diary.builder().id(2L).title("test").content("diary").build();

        when(diaryRepository.findAllByMemberId(any())).thenReturn(List.of(diary1, diary2));

        diaryService.findDiaries(1L);

        verify(diaryRepository).findAllByMemberId(any());
    }

    @Test
    void findDiary() {
        when(diaryRepository.findById(any())).thenReturn(Optional.of(diary));

        diaryService.findDiary(1L);

        verify(diaryRepository).findById(any());
    }

    @Test
    void addDiary() throws IOException {
        String imagePath = "/hello/test.jpg";
        when(uploadService.storeFile(any())).thenReturn(imagePath);
        diaryService.addDiary(request, member);

        assertAll(
            () -> verify(uploadService).storeFile(any()),
            () -> verify(diaryRepository).save(any())
        );
    }

    @Test
    void addDuplicateDateDiary() {
        when(diaryRepository.findAllByMemberId(any())).thenReturn(List.of(diary));
        assertThatThrownBy(() -> diaryService.addDiary(request, member))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void updateDiary() {
        DiaryRequest updateRequest = DiaryRequest.builder()
            .title("hello")
            .feel("Good")
            .build();
        diary.associateMember(member);
        when(diaryRepository.findById(any())).thenReturn(Optional.of(diary));

        diaryService.updateDiary(1L, updateRequest, member);

        assertAll(
            () -> assertThat(diary.getTitle()).isEqualTo(updateRequest.getTitle()),
            () -> assertThat(diary.getFeel()).isEqualTo(updateRequest.getFeel()),
            () -> verify(diaryRepository).findById(any())
        );
    }

    @Test
    void updateWithNotOwner() {
        DiaryRequest updateRequest = DiaryRequest.builder()
            .title("hello")
            .feel("Good")
            .build();
        Member notOwner = Member.builder()
            .id(2L)
            .build();
        diary.associateMember(member);
        when(diaryRepository.findById(any())).thenReturn(Optional.of(diary));

        assertThatThrownBy(() -> diaryService.updateDiary(1L, updateRequest, notOwner))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deleteDiary() {
        when(diaryRepository.findById(any())).thenReturn(Optional.of(diary));

        diaryService.deleteDiary(1L);

        assertAll(
            () -> verify(diaryRepository).findById(any()),
            () -> verify(uploadService).deleteFile(any()),
            () -> assertThat(diary.getImgPath()).isNull()
        );
    }

    @Test
    void updateDiaryImage() throws IOException {
        String newPath = "/hello/t.jpg";
        when(diaryRepository.findById(any())).thenReturn(Optional.of(diary));
        when(uploadService.storeFile(any())).thenReturn(newPath);
        diary.associateMember(member);

        diaryService.updateDiaryImage(1L, new MockMultipartFile("t.jpg", "11".getBytes()), member);

        assertAll(
            () -> assertThat(diary.getImgPath()).isEqualTo(newPath),
            () -> verify(diaryRepository).findById(any()),
            () -> verify(uploadService).storeFile(any())
        );
    }

    @Test
    void getImage() {
        diaryService.getImageFullPath("test.jpg");
        verify(uploadService).getFullPath(any());
    }
}
