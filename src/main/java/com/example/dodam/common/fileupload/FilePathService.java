package com.example.dodam.common.fileupload;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FilePathService {

    private final FilePath filePath;

    public String selectImagePath(ImageType imageType) {
        return matchTypeAndPath(imageType);
    }

    public String getFullPath(String filename, ImageType type) {
        return matchTypeAndPath(type) + filename;
    }

    private String matchTypeAndPath(ImageType imageType) {
        if (imageType.equals(ImageType.PROFILE)) {
            return filePath.getProfile();
        } else if (imageType.equals(ImageType.DIARY)) {
            return filePath.getDiary();
        }
        return null;
    }
}
