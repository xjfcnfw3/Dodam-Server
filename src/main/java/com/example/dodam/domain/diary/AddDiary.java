
package com.example.dodam.domain.diary;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

//다이어리 등록에 사용
@Getter
@Setter
public class AddDiary {
    public Integer userId;
    public Date date;
    public String title;
    public String base64Img;
    public String oneWord;
    public String feel;
    public String content;

}

