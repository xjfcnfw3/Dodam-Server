package com.example.dodam.service.user;

import static com.example.dodam.common.exception.ErrorCode.FAIL_VERIFY;
import static com.example.dodam.common.exception.ErrorCode.NOT_SEND_MESSAGE;
import static com.example.dodam.common.exception.ErrorCode.NOT_VERIFIED_PHONE;
import static com.example.dodam.domain.sms.VerificationStatus.UNVERIFIED;
import static com.example.dodam.domain.sms.VerificationStatus.VERIFIED;

import com.example.dodam.common.exception.RegisterException;
import com.example.dodam.domain.sms.Verification;
import com.example.dodam.domain.sms.dto.VerificationResponse;
import com.example.dodam.repository.sms.VerifyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * 문자 인증 처리에 관한 객체
 * - 전화번호와 인증번호 저장
 * - 인증 번호 확인 (5분 기간)
 * - 회원 가입전 인증된 번호인지 확인 (30분 기간)
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class VerifyService {

    private final VerifyRepository repository;


    /**
     * redis에 저장된 코드 값 확인
     * @param phoneNumber 휴대폰 번호
     * @param code 인증 번호
     * @return response
     */
    public VerificationResponse verifyCode(String phoneNumber, String code) {
        Verification verification = repository.findById(phoneNumber)
            .orElseThrow(() -> new RegisterException(NOT_SEND_MESSAGE));

        if (!verification.getCode().equals(code)) {
            throw new RegisterException(FAIL_VERIFY);
        }
        verification.setStatus(VERIFIED);
        log.debug("verification = {}", verification);
        repository.save(verification);
        return new VerificationResponse("인증이 성공했습니다.");
    }

    /**
     * 해당 번호가 인증된 번호인지 확인
     * @param phoneNumber 휴대폰 번호
     */
    public void checkVerifiedNumber(String phoneNumber) {
        Verification verification = repository.findById(phoneNumber)
            .orElseThrow(() -> new RegisterException(NOT_VERIFIED_PHONE));
        if (!verification.getStatus().equals(VERIFIED)) {
            throw new RegisterException(NOT_VERIFIED_PHONE);
        }
        repository.delete(verification);
    }

    /**
     * 휴대폰 번호를 key, 코드를 value로 저장
     * @param phoneNumber 휴대폰 번호
     * @param code 인증 번호
     */
    public void saveCode(String phoneNumber, String code) {
        Verification verification = new Verification();
        verification.setId(phoneNumber);
        verification.setStatus(UNVERIFIED);
        verification.setCode(code);
        repository.save(verification);
    }
}
