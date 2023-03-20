package com.example.dodam.service;

import com.example.dodam.service.user.VerifyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class VerifyServiceTest {

    @Autowired
    private VerifyService service;


    @Test
    void verify() {
        String phoneNumber = "01012345678";
        String code = "12345";
        service.saveCode(phoneNumber, code);
        service.verifyCode(phoneNumber, code);
        service.checkVerifiedNumber(phoneNumber);
    }
}
