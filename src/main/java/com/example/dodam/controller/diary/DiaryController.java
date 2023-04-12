package com.example.dodam.controller.diary;

import com.example.dodam.config.security.LoginMember;
import com.example.dodam.domain.diary.dto.DiaryRequest;
import com.example.dodam.domain.diary.dto.DiaryDetailsResponse;
import com.example.dodam.domain.diary.dto.DiaryResponse;
import com.example.dodam.domain.member.Member;
import com.example.dodam.service.diary.DiaryService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/diary")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService ;

    @GetMapping
    public List<DiaryResponse> getDiaryList(@LoginMember Member member){
        return DiaryResponse.listOf(member.getDiaries());
    }

    @GetMapping("/{id}")
    public DiaryDetailsResponse getDiaryDetail(@PathVariable Long id){
        return DiaryDetailsResponse.of(diaryService.findDiary(id));
    }

    @GetMapping(value = "/image/{filename}", produces = MediaType.IMAGE_JPEG_VALUE)
    public Resource downloadDiaryImage(@PathVariable String filename) {
        try {
            return new UrlResource("file:" + diaryService.getImageFullPath(filename));
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
            throw new IllegalArgumentException("이미지 다운로드를 실패했습니다.");
        }
    }

    @PostMapping
    public DiaryDetailsResponse addDiary(@ModelAttribute DiaryRequest request, @LoginMember Member member){
        return DiaryDetailsResponse.of(diaryService.addDiary(request, member));
    }

    @PatchMapping("/{id}")
    public void updateDiaryContent(@PathVariable Long id, @RequestBody DiaryRequest request,
        @LoginMember Member member) {
        diaryService.updateDiaryContent(id, request, member);
    }

    @PatchMapping("/image/{id}")
    public void updateDiaryImage(@PathVariable Long id, MultipartFile image, @LoginMember Member member) {
        diaryService.updateDiaryImage(id, image, member);
    }

    @DeleteMapping("/{id}")
    public void deleteDiary(@PathVariable Long id, @LoginMember Member member){
        diaryService.deleteDiary(id, member);
    }

    @DeleteMapping("image/{id}")
    public void deleteDiaryImage(@PathVariable Long id, @LoginMember Member member) {
        diaryService.deleteDiaryImage(id, member);
    }
}
