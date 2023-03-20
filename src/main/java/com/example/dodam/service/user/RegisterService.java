package com.example.dodam.service.user;

import com.example.dodam.common.exception.RegisterException;
import com.example.dodam.common.exception.ErrorCode;
import com.example.dodam.domain.user.RegisterRequest;
import com.example.dodam.domain.user.User;
import com.example.dodam.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterService {

    private final UserRepository userRepository;

    public User register(RegisterRequest registerRequest) {
        validateRegisterRequest(registerRequest);
        User user = extractUser(registerRequest);
        log.debug("user = {}", user);
        return userRepository.save(user);
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
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RegisterException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    private void validateDuplicateNickName(String nickname) {
        if (userRepository.findByNickName(nickname).isPresent()) {
            throw new RegisterException(ErrorCode.DUPLICATED_NICKNAME);
        }
    }

    private User extractUser(RegisterRequest request) {
        User user = request.toUser();
        user.setRole("ROLE_USER");
        user.setStatus("A");
        return user;
    }
}
