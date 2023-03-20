package com.example.dodam.controller.user;

import com.example.dodam.domain.sms.dto.SmsResponse;
import com.example.dodam.domain.sms.dto.VerificationRequest;
import com.example.dodam.domain.sms.dto.VerificationResponse;
import com.example.dodam.domain.user.RegisterRequest;
import com.example.dodam.domain.user.UserResponse;
import com.example.dodam.service.user.FileUploadService;
import com.example.dodam.service.user.RegisterService;
import com.example.dodam.service.user.SmsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("user/register")
public class RegisterController {

    private final RegisterService registerService;
    private final FileUploadService fileUploadService;
    private final SmsService smsService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/sms")
    public ResponseEntity<SmsResponse> getSms(@RequestParam String phone)
        throws URISyntaxException, JsonProcessingException {
        SmsResponse response = smsService.sendSms(phone);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/verify")
    public ResponseEntity<VerificationResponse> verifyNumber(@RequestBody VerificationRequest request) {
        log.debug("request={} {}", request.getCode(), request.getPhone());
        VerificationResponse response = smsService.verifyCode(request.getPhone(), request.getCode());
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public Object register(@ModelAttribute RegisterRequest request) throws IOException {
        String imagePath = fileUploadService.storeFile(request.getProfileImage());
        request.setImagePath(imagePath);
        try {
            smsService.checkPhone(request.getPhone());
            request.setPassword(passwordEncoder.encode(request.getPassword()));
            registerService.register(request);
            return Map.of("result", "성공");
        } catch (Exception e) {
            fileUploadService.deleteFile(imagePath);
            throw e;
        }
    }

    @PostMapping("/email")
    public Object checkEmail(@RequestParam String email) {
        registerService.checkDuplicateEmail(email);
        return Map.of("result", "중복되지 않은 이메일입니다.");
    }

    @PostMapping("/nickname")
    public Object checkNickname(@RequestParam String nickname) {
        registerService.checkDuplicateNickname(nickname);
        return Map.of("result", "중복되지 않은 닉네임입니다.");
    }
}
