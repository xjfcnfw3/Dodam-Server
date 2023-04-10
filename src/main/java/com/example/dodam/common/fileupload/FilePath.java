package com.example.dodam.common.fileupload;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Getter
@Component
public class FilePath {

    @Value("${file.profile}")
    private String profile;

    @Value("${file.diary}")
    private String diary;

}
