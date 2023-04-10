package com.example.dodam.common.fileupload.service;

import com.example.dodam.common.fileupload.FilePathService;
import com.example.dodam.common.fileupload.ImageType;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 저장 관련 클래스
 */
@Slf4j
@RequiredArgsConstructor
public class FileUploadService {

    private final FilePathService filePathService;
    private final ImageType imageType;

    public String getFullPath(String filename) {
        return filePathService.getFullPath(filename, imageType) + filename;
    }

    public String storeFile(MultipartFile multipartFile) throws IOException {

        if (validate(multipartFile)) {
            String originalFilename = multipartFile.getOriginalFilename();
            String storeFileName = createStoreFileName(originalFilename);
            log.info("File storage path={}", storeFileName);
            multipartFile.transferTo(new File(getFullPath(storeFileName)));
            return storeFileName;
        }
        return null;
    }

    public void deleteFile(String storeFilename) {
        File file = new File(getFullPath(storeFilename));
        if (!file.delete()) {
            log.warn("File not Found!");
        }
    }

    private boolean validate(MultipartFile multipartFile) {
        if (multipartFile == null) {
            return false;
        }
        return !multipartFile.isEmpty();
    }

    protected String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 파일 형식 추출 ex) png, jpg
    protected String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
