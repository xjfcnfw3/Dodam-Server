package com.example.dodam.controller.inquiry;

import com.example.dodam.domain.inquiry.Inquiry;
import com.example.dodam.domain.user.User;
import com.example.dodam.service.inquiry.InquiryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class InquiryController {

    private final InquiryService inquiryService;

    public InquiryController(InquiryService inquiryService) {
        this.inquiryService = inquiryService;
    }

    // GET 전체 게시물 조회 -> 전체 게시물 정보 확인
    // GET 상세 게시물 조회 -> 게시글 상세 조회
    // POST 게시글 작성
    // PUT 게시글 수정
    // DELETE 게시글 삭제


    // localhost:8080/inquiries
    // 전체 게시물 조회
    @GetMapping("/inquiries")
    public ResponseEntity<?> getInquries() {
        return new ResponseEntity<>(inquiryService.getInquiries(), HttpStatus.OK);
    }


    // ex) localhost:8080/inquiry/3
    // 단건 게시글 조회
    @GetMapping("/inquiry/{id}")
    public ResponseEntity<?> getInquiry(@PathVariable("id") Long id) {
        return new ResponseEntity<>(inquiryService.getInquiry(id), HttpStatus.OK);
    }

//    @PostMapping("/inquiry/new")
//    public ResponseEntity<?> save(@RequestBody Inquiry inquiry) {
//        return new ResponseEntity<>(inquiryService.save(inquiry), HttpStatus.CREATED);
//    }


// localhost:8080/inquiry/new (Only POST)
// POST 게시글 작성
    @PostMapping(value = "/inquiry/new", consumes="multipart/form-data") //file
    public ResponseEntity<?> save(@ModelAttribute Inquiry inquiry, @Nullable MultipartFile file) throws Exception{
        return new ResponseEntity<>(inquiryService.save(inquiry, file), HttpStatus.CREATED);
    }

    // PUT 게시글 수정
    // ex) localhost:8080/inquiry/3
    // 게시글 수정하고 -> 완료 버튼을 누른다 -> 백엔드 서버 요청 (id, updateInquiry)
    @PutMapping("/inquiry/{id}")
    public ResponseEntity<?> update(@PathVariable("id") Long id, Inquiry updateInquiry, @Nullable MultipartFile file) throws IOException {
        return new ResponseEntity<>(inquiryService.update(id, updateInquiry, file), HttpStatus.OK);
    }


    // DELETE 게시글 삭제
    // ex) localhost:8080/boards/3
    // 게시글 삭제하기
    @DeleteMapping("/inquiry/{id}")
    public ResponseEntity<?> deleteInquiry(@PathVariable("id") Long id) {
        inquiryService.deleteInquiry(id);
        return new ResponseEntity<>("삭제 완료", HttpStatus.OK);
    }
}