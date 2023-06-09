package com.example.dodam.service.diary;

import com.example.dodam.common.fileupload.service.DiaryImageUploadService;
import com.example.dodam.domain.diary.*;
import com.example.dodam.domain.diary.dto.DiaryRequest;
import com.example.dodam.domain.diary.dto.DiaryResponse;
import com.example.dodam.domain.member.Member;
import com.example.dodam.repository.diary.DiaryRepository;
import java.time.LocalDate;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final DiaryImageUploadService uploadService;

    public List<DiaryResponse> findDiaries(Long id){
        return DiaryResponse.listOf(diaryRepository.findAllByMemberId(id));
    }

    public Diary findDiary(Long id){
        return diaryRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    @Transactional
    public Diary addDiary(DiaryRequest request, Member member){
        Diary diary = request.toDiary();
        diary.associateMember(member);
        validateDuplicateDiary(diary, member);
        saveDiaryImageFile(request.getDiaryImage(), diary);
        return diaryRepository.save(diary);
    }

    @Transactional
    public void updateDiaryContent(Long diaryId, DiaryRequest request, Member member) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(EntityNotFoundException::new);
        Diary updateDiary = request.toDiary();

        if (isDiaryOwner(member, diary)) {
            diary.updateDiaryContent(updateDiary);
        } else {
            throw new IllegalArgumentException("일기장의 주인이 아닙니다.");
        }
    }

    @Transactional
    public void updateDiaryImage(Long diaryId, MultipartFile multipartFile, Member member) {
        Diary diary = diaryRepository.findById(diaryId).orElseThrow(EntityNotFoundException::new);
        String originPath = diary.getImgPath();
        if (isDiaryOwner(member, diary)) {
            saveDiaryImageFile(multipartFile, diary);
            uploadService.deleteFile(originPath);
        } else {
            throw new IllegalArgumentException("일기장의 주인이 아닙니다.");
        }
    }

    @Transactional
    public void deleteDiary(Long id, Member member){
        diaryRepository.findById(id).ifPresent(value -> {
            if (isDiaryOwner(member, value)) {
                uploadService.deleteFile(value.getImgPath());
                member.deleteDiary(value);
            } else {
                throw new IllegalArgumentException("일기장의 주인이 아닙니다.");
            }
        });
    }

    public String getImageFullPath(String imagePath) {
        return uploadService.getFullPath(imagePath);
    }

    @Transactional
    public void deleteDiaryImage(Long id, Member member) {
        diaryRepository.findById(id).ifPresent(value -> {
            if (isDiaryOwner(member, value)) {
                uploadService.deleteFile(value.getImgPath());
                value.deleteImagePath();
            } else {
                throw new IllegalArgumentException("일기장의 주인이 아닙니다.");
            }
        });
    }

    private boolean isDiaryOwner(Member member, Diary diary) {
        return member.getId().equals(diary.getMember().getId());
    }

    private void validateDuplicateDiary(Diary diary, Member member) {
        Optional<LocalDate> date = diaryRepository.findAllByMemberId(member.getId()).stream()
            .map(Diary::getDate)
            .filter((d) -> diary.getDate().equals(d))
            .findAny();
        
        if (date.isPresent()) {
            throw new IllegalArgumentException("이미 날짜가 같은 일기장이 있습니다.");
        }
    }

    private void saveDiaryImageFile(MultipartFile file, Diary diary) {
        try {
            String filePath = uploadService.storeFile(file);
            diary.updateImagePath(filePath);
        } catch (IOException e) {
            log.error("Error={}", e.getLocalizedMessage());
            throw new IllegalArgumentException(e.getLocalizedMessage());
        }
    }
}
