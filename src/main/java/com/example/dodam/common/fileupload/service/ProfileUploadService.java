package com.example.dodam.common.fileupload.service;

import com.example.dodam.common.fileupload.FilePathService;
import com.example.dodam.common.fileupload.ImageType;
import org.springframework.stereotype.Service;

@Service
public class ProfileUploadService extends FileUploadService{

    public ProfileUploadService(FilePathService filePathService) {
        super(filePathService, ImageType.PROFILE);
    }

}
