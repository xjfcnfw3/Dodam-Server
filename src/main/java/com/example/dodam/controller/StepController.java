package com.example.dodam.controller;

import com.example.dodam.config.auth.MemberDetails;
import com.example.dodam.domain.member.Member;
import com.example.dodam.dto.*;
import com.example.dodam.service.StepService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/step")
@RequiredArgsConstructor
public class StepController {

    private final StepService stepService;

    @GetMapping("/main")
    public MainStepResponse getMainStep(Authentication authentication) {
        Member member = getPrincipalUser(authentication);
        return stepService.getMainStep(member);
    }

    @GetMapping("/enroll")
    public StepEnrollResponse getStepEnroll(Authentication authentication) {
        Member member = getPrincipalUser(authentication);
        return stepService.getStepEnroll(member);
    }


    @GetMapping("/{id}")
    public StepResponse getStep(@PathVariable Long id){
        return StepResponse.of(stepService.getStep(id));
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody StepRequest dto){
        stepService.updateStep(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        stepService.delete(id);
    }

    @PostMapping
    public StepResponse addStep(@RequestBody StepRequest request,Authentication authentication){
        Member member = getPrincipalUser(authentication);
        return StepResponse.of(stepService.addStep(member.getId(), request));
    }

    private Member getPrincipalUser(Authentication authentication) {
        MemberDetails principal = (MemberDetails) authentication.getPrincipal();
        return principal.getMember();
    }
}
