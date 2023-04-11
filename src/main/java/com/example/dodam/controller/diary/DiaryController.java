package com.example.dodam.controller.diary;

import com.example.dodam.config.auth.MemberDetails;
import com.example.dodam.domain.diary.dto.DiaryRequest;
import com.example.dodam.domain.diary.dto.DiaryDetail;
import com.example.dodam.domain.diary.dto.DiaryDetailImg;
import com.example.dodam.domain.diary.dto.DiaryResponse;
import com.example.dodam.service.diary.DiaryService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
public class DiaryController {

    private final DiaryService diaryService ;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }



    // 다이어리 목록 조회
    @GetMapping("/diary/{id}")
    public List<DiaryResponse> getDiaryList(@PathVariable Long id){
        return diaryService.findDiaries(id);
    }

    // 다이어리 디테일 조회
    @GetMapping("/diary/detail/{id}")
    public Optional<DiaryDetailImg> getDiaryDetail(@PathVariable Long id){
        Optional<DiaryDetail> diary = diaryService.findDiary(id);
        DiaryDetailImg diaryDetailImg = new DiaryDetailImg();
        diaryDetailImg.setId(diary.get().getId());
        diaryDetailImg.setTitle(diary.get().getTitle());
        diaryDetailImg.setContent(diary.get().getContent());
        diaryDetailImg.setOneWord(diary.get().getOneWord());
        try {
            String img =  diaryService.getImageFullPath(diary.get().getImgPath());

            diaryDetailImg.setImg(img);

        }
        catch (Exception e) {
            System.out.print("이미지 조회 실패");
        }
        return Optional.of(diaryDetailImg);
    }

    //다이어리 등록
    @PostMapping("/diary")
    public String addDiary(@RequestBody DiaryRequest request, Authentication authentication){
        MemberDetails details = (MemberDetails) authentication.getPrincipal();
        diaryService.addDiary(request, details.getUser());
        return "성공";
    }
    //수정
    @PutMapping("/diary/{id}")
    public String putDiary(@PathVariable("id") Long id, @RequestBody DiaryRequest request, Authentication authentication){
        MemberDetails details = (MemberDetails) authentication.getPrincipal();
        diaryService.updateDiary(id, request, details.getUser());
        return "성공";
    }

    //삭제
    @DeleteMapping("/diary/{id}")
    public String deleteDiary(@PathVariable Long id){
        //해당 id 다이어리 존재 여부 확인
        diaryService.deleteDiary(id);
        return "성공";
    }
    // 다이어리 이미지만 추가 (Test용)
//    //
//    @PostMapping("/diary/img/")
//    public String addDiary( @RequestBody MultipartFile img) throws Exception {
////        Integer id = diaryimg.getId();
////        MultipartFile img = diaryimg.getImg();
//        System.out.printf("pass");
//        diaryService.saveDiaryImag(img);
//        System.out.printf("pass");
//
//        return "성공";
//    }

}
