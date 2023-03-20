package com.example.dodam.service.user;

import com.example.dodam.common.exception.EntityNotFoundException;
import com.example.dodam.common.exception.ErrorCode;
import com.example.dodam.domain.user.UpdateUserRequest;
import com.example.dodam.domain.user.User;
import com.example.dodam.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    public User update(String email, UpdateUserRequest updateUserRequest) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException(ErrorCode.USER_NOT_FOUND));
        return userRepository.update(user.getId(), updateUserRequest.toUser());
    }

    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }

    public void deleteImage(Long userId) {
        userRepository.deleteImage(userId);
    }
}
