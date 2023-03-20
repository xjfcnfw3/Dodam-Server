package com.example.dodam.service.member;

import com.example.dodam.domain.sms.dto.SmsMessage;
import com.example.dodam.domain.sms.dto.SmsRequest;
import com.example.dodam.domain.sms.dto.SmsResponse;
import com.example.dodam.domain.sms.dto.VerificationResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 휴대폰 문자 인증을 위한 클래스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SmsService {
    private static final String SPACE = " ";
    private static final String NEWLINE = "\n";
    private static final String METHOD = "POST";
    private static final String FRONT_URL = "/sms/v2/services/";
    private static final String BACK_URL = "/messages";
    private static final String CONTENT_BEFORE_NUMBER = "[도담] 인증번호\n[";
    private static final String CONTENT_AFTER_NUMBER = "]를 입력해 주세요";

    private final VerifyService verifyService;

    @Value("${sms.service-id}")
    private String serviceId;
    @Value("${sms.access-key}")
    private String accessKey;
    @Value("${sms.secret-key}")
    private String secretKey;
    @Value("${sms.phone}")
    private String phoneNumber;
    @Value("${sms.set}")
    private boolean usingApi;


    /**
     * 문자 전송 메서드
     * usingApi가 true면 문자가 false면 response로 번호가 전송
     * @param phoneNumber (인증 문자를 받을 휴대폰 번호)
     * @return VerificationResponse (네이버 API에서 받는 response or 어플리케이션에 직접 만든 response)
     */
    public SmsResponse sendSms(String phoneNumber)
        throws JsonProcessingException, URISyntaxException {
        Long time = System.currentTimeMillis();
        String code = generateCode();

        String jsonBody = generateBody(phoneNumber, code);
        HttpHeaders headers = generateHeaders(time);

        return receiveResponse(jsonBody, headers, code);
    }

    /**
     * @param phoneNumber (문자에 전송된 번호)
     * @param code (인증 번호)
     * @return 확인 response
     */
    public VerificationResponse verifyCode(String phoneNumber, String code) {
        return verifyService.verifyCode(phoneNumber, code);
    }

    /**
     * 회원 가입전 해당 번호로 인증되었는지 확인
     * @param phoneNumber (휴대폰 번호)
     */
    public void checkPhone(String phoneNumber) {
        verifyService.checkVerifiedNumber(phoneNumber);
    }

    private String makeSignature(Long time) {
        String message = generateMessage(time);
        return encodeMessage(message);
    }

    private HttpHeaders generateHeaders(Long time) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-ncp-apigw-timestamp", time.toString());
        headers.set("x-ncp-iam-access-key", accessKey);
        headers.set("x-ncp-apigw-signature-v2", makeSignature(time));
        return headers;
    }

    private SmsResponse receiveResponse(String jsonBody, HttpHeaders headers, String code) throws URISyntaxException {
        HttpEntity<String> body = new HttpEntity<>(jsonBody, headers);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        if (!usingApi) {
            return new SmsResponse("", LocalDateTime.now(),
                "200", "테스트용 코드 " + code);
        }

        return restTemplate.postForObject(
            new URI("https://sens.apigw.ntruss.com/sms/v2/services/"+ this.serviceId+"/messages"),
            body, SmsResponse.class);
    }

    private String generateBody(String recipientPhoneNumber, String code) throws JsonProcessingException {
        List<SmsMessage> smsMessages = new ArrayList<>();
        log.debug("code = {}", code);
        verifyService.saveCode(recipientPhoneNumber, String.valueOf(code));
        SmsMessage smsMessage = new SmsMessage(recipientPhoneNumber, "");
        smsMessages.add(smsMessage);
        SmsRequest smsRequest = new SmsRequest("SMS", "COMM", "82",
            phoneNumber, CONTENT_BEFORE_NUMBER + code + CONTENT_AFTER_NUMBER, smsMessages);
        return new ObjectMapper().writeValueAsString(smsRequest);
    }

    private String generateCode() {
        return String.valueOf((int) Math.floor(Math.random() * (999999 - 100000)) + 100000);
    }

    private String encodeMessage(String message) {
        try {
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(key);
            byte[] rawHmac = mac.doFinal(message.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64String(rawHmac);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String generateMessage(Long time) {
        return new StringBuilder()
            .append(METHOD)
            .append(SPACE)
            .append(FRONT_URL)
            .append(serviceId)
            .append(BACK_URL)
            .append(NEWLINE)
            .append(time.toString())
            .append(NEWLINE)
            .append(accessKey)
            .toString();
    }
}
