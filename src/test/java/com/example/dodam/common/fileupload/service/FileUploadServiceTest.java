package com.example.dodam.common.fileupload.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.example.dodam.common.fileupload.FilePathService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FileUploadServiceTest {

    private static final String PATH = "/test/";

    private FileUploadService fileUploadService;

    @Mock
    private FilePathService filePathService;


    @BeforeEach
    void init() {
        fileUploadService = new FileUploadService(filePathService, null);
    }

    @Test
    void getFullPath() {
        String filename = "hello";
        when(filePathService.getFullPath(any(), any())).thenReturn(PATH);

        String fullPath = fileUploadService.getFullPath(filename);

        assertThat(fullPath).isEqualTo(PATH + filename);
        verify(filePathService).getFullPath(any(), any());
    }
}
