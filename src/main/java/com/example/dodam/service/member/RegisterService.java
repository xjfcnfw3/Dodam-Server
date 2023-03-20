package com.example.dodam.service.member;

import com.example.dodam.common.exception.RegisterException;
import com.example.dodam.common.exception.ErrorCode;
import com.example.dodam.domain.member.RegisterRequest;
import com.example.dodam.domain.member.Member;
import com.example.dodam.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService {

    private final MemberRepository memberRepository;

    public Member register(RegisterRequest registerRequest) {
        validateRegisterRequest(registerRequest);
        Member member = extractUser(registerRequest);
        log.debug("user = {}", member);
        return memberRepository.save(member);
    }

    public void checkDuplicateEmail(String email) {
        validateDuplicateEmail(email);
    }

    public void checkDuplicateNickname(String nickname) {
        validateDuplicateNickName(nickname);
    }

    private void validateRegisterRequest(RegisterRequest registerRequest) {
        validateDuplicateEmail(registerRequest.getEmail());
        validateDuplicateNickName(registerRequest.getNickname());
    }

    private void validateDuplicateEmail(String email) {
        if (memberRepository.findByEmail(email).isPresent()) {
            throw new RegisterException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    private void validateDuplicateNickName(String nickname) {
        if (memberRepository.findByNickName(nickname).isPresent()) {
            throw new RegisterException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }

    private Member extractUser(RegisterRequest request) {
        Member member = request.toUser();
        member.setRole("ROLE_USER");
        member.setStatus("A");
        return member;
    }
}
