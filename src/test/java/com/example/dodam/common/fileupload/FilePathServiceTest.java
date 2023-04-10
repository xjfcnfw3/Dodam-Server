package com.example.dodam.common.fileupload;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FilePathServiceTest {

    @Mock
    private FilePath filePath;

    private FilePathService pathService;

    @BeforeEach
    void init() {
        pathService = new FilePathService(filePath);
    }

    @DisplayName("이미지 타입이 입력되면 저장될 기본경로가 출력된다.")
    @Test
    void selectPath() {
        String profilePath = "/profile";
        String diaryPath = "/diary";

        when(filePath.getDiary()).thenReturn(diaryPath);
        when(filePath.getProfile()).thenReturn(profilePath);

        assertAll(
            () -> {
                String path = pathService.selectImagePath(ImageType.PROFILE);
                assertThat(path).isEqualTo(profilePath);
            },
            () -> {
                String path = pathService.selectImagePath(ImageType.DIARY);
                assertThat(path).isEqualTo(diaryPath);
            }
        );
    }
}
