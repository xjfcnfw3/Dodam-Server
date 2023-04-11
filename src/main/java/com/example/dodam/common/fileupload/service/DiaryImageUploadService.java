package com.example.dodam.common.fileupload.service;

import com.example.dodam.common.fileupload.FilePathService;
import com.example.dodam.common.fileupload.ImageType;
import org.springframework.stereotype.Service;

@Service
public class DiaryImageUploadService extends FileUploadService {

    public DiaryImageUploadService(FilePathService filePathService) {
        super(filePathService, ImageType.DIARY);
    }
}
