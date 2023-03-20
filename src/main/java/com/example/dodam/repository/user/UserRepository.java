package com.example.dodam.repository.user;

import com.example.dodam.domain.user.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository {
	Optional<User> findByUsername(String email);
    User save(User user);
    Optional<User> findByEmail(String email);
    void deleteById(Long userId);
    User update(Long userId, User user);
    Optional<User> findByNickName(String nickname);
    Optional<User> findById(Long id);
    void deleteImage(Long userId);
    void updateStartDate(Long userId, LocalDateTime startDate);
}
