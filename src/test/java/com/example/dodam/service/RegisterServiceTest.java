package com.example.dodam.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.dodam.common.exception.RegisterException;
import com.example.dodam.domain.user.RegisterRequest;
import com.example.dodam.domain.user.User;
import com.example.dodam.repository.user.UserRepository;
import com.example.dodam.service.user.RegisterService;
import java.time.LocalDate;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class RegisterServiceTest {

    @Autowired
    private RegisterService registerService;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Test user register")
    @MethodSource("generateTestRegisterRequest")
    @ParameterizedTest
    void register(RegisterRequest registerRequest) {
        User user = registerService.register(registerRequest);
        User savedUser = userRepository.findById(user.getId()).get();
        assertThat(savedUser.getCreateAt()).isNotNull();
    }

    @DisplayName("Input Duplicate email user")
    @MethodSource("generateTestRegisterRequest")
    @ParameterizedTest
    void registerDuplicateEmail(RegisterRequest registerRequest) {
        RegisterRequest duplicatedRequest = RegisterRequest.builder().email("test@naver.com").password("123")
            .nickname("hello2").birthDate(LocalDate.of(2023, 1, 29)).phone("012345678")
            .build();
        registerService.register(registerRequest);
        assertThatThrownBy(() ->registerService.register(duplicatedRequest))
            .isInstanceOf(RegisterException.class);
    }

    @DisplayName("Input Duplicate nickname user")
    @MethodSource("generateTestRegisterRequest")
    @ParameterizedTest
    void registerDuplicateNickname(RegisterRequest registerRequest) {
        RegisterRequest duplicatedRequest = RegisterRequest.builder().email("test2@naver.com").password("123")
            .nickname("hello").birthDate(LocalDate.of(2023, 1, 29)).phone("012345678")
            .build();
        registerService.register(registerRequest);
        assertThatThrownBy(() -> registerService.register(duplicatedRequest))
            .isInstanceOf(RegisterException.class);
    }

    @MethodSource("generateTestRegisterRequest")
    @ParameterizedTest
    void inputDelicateEmail(RegisterRequest registerRequest) {
        registerService.register(registerRequest);
        assertThatThrownBy(() -> registerService.checkDuplicateEmail(registerRequest.getEmail()))
            .isInstanceOf(RegisterException.class);
    }

    @MethodSource("generateTestRegisterRequest")
    @ParameterizedTest
    void inputDelicateNickname(RegisterRequest registerRequest) {
        registerService.register(registerRequest);
        assertThatThrownBy(() -> registerService.checkDuplicateNickname(registerRequest.getNickname()))
            .isInstanceOf(RegisterException.class);
    }

    private static Stream<RegisterRequest> generateTestRegisterRequest() {
        return Stream.of(
            RegisterRequest.builder().email("test@naver.com").password("123").nickname("hello")
            .birthDate(LocalDate.of(2023, 1, 29)).phone("012345678")
            .build());
    }
}
