package com.example.dodam.common.fileupload.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 파일 저장 관련 클래스
 */
@Slf4j
@Service
public class FileUploadService {

    // properties 파일에 file.dir에 설정
    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public String storeFile(MultipartFile multipartFile) throws IOException {

        if (multipartFile == null) {
            return null;
        } else if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String storeFileName = createStoreFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return storeFileName;
    }

    public void deleteFile(String storeFilename) {
        File file = new File(getFullPath(storeFilename));
        boolean delete = file.delete();
    }

    // 저장할 파일 이름 생성 UUID
    private String createStoreFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 파일 형식 추출 ex) png, jpg
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
