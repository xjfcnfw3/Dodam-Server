package com.example.dodam.controller;

import com.example.dodam.config.auth.MemberDetails;
import com.example.dodam.domain.member.Member;
import com.example.dodam.dto.*;
import com.example.dodam.domain.Step;
import com.example.dodam.service.StepService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/step")
@RequiredArgsConstructor
public class StepController {

    private final StepService stepService;

    @GetMapping("/main")
    public StepMainDto main(Authentication authentication) {
        Member member = getPrincipalUser(authentication);
        return stepService.getMainStep(member);
    }

    @GetMapping("/enroll")
    public StepEnrollDto getStepEnroll(Authentication authentication) {
        Member member = getPrincipalUser(authentication);
        return stepService.getStepEnroll(member);
    }

    @PutMapping("/enroll")
    public @ResponseBody ResponseEntity<Object> changeOrder(@RequestBody StepChangeOrderDto dto,Authentication authentication){
        Member member = getPrincipalUser(authentication);
        Long userId = member.getId();
        stepService.changeOrder(userId, dto.getFirstOrder(), dto.getSecondOrder());
        return new ResponseEntity<>(Map.of("result", "순서 변경 성공"), HttpStatus.OK);
    }

    @PutMapping("/startDate/{startDate}")
    public @ResponseBody ResponseEntity<Object> setStartDate(@PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate, Authentication authentication){
        Member member = getPrincipalUser(authentication);
        stepService.setStartDate(startDate.atStartOfDay(), member.getId());
        return new ResponseEntity<>(Map.of("result", "시작 날짜 설정 성공"), HttpStatus.OK);
    }

    @GetMapping("/select")
    public Step getStep(Integer stepId){
        return stepService.getStep(stepId);
    }

    @PutMapping("/select")
    public @ResponseBody ResponseEntity<Object> change(@RequestBody StepSelectDto dto){
        stepService.changeStep(dto);
        return new ResponseEntity<>(Map.of("result", "단계 수정 성공"), HttpStatus.OK);
    }

    @DeleteMapping("/select/{stepId}")
    public @ResponseBody ResponseEntity<Object> delete(@PathVariable Integer stepId){
        stepService.delete(stepId);
        return new ResponseEntity<>(Map.of("result", "단계 삭제 성공"), HttpStatus.OK);
    }

    @PostMapping("")
    public @ResponseBody ResponseEntity<Object> add(@RequestBody StepAddDto dto,Authentication authentication){
        Member member = getPrincipalUser(authentication);
        Long userId = member.getId();
        stepService.addStep(userId, dto);
        return new ResponseEntity<>(Map.of("result", "단계 생성 성공"), HttpStatus.CREATED);
    }

    private Member getPrincipalUser(Authentication authentication) {
        MemberDetails principal = (MemberDetails) authentication.getPrincipal();
        return principal.getUser();
    }
}
