package com.example.dodam.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.example.dodam.domain.user.UpdateUserRequest;
import com.example.dodam.domain.user.User;
import com.example.dodam.repository.user.UserRepository;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DataSource dataSource;
    private JdbcTemplate template;

    @BeforeEach
    void initGeneratedId() {
        template = new JdbcTemplate(dataSource);
        template.execute("ALTER TABLE USER auto_increment = 1");
    }

    @DisplayName("Test user save")
    @MethodSource("generateTestUsers")
    @ParameterizedTest
    void save(User user) {
        User savedUser = userRepository.save(user);
        User foundUser = userRepository.findById(user.getId()).get();
        assertThat(foundUser.getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.getCreateAt()).isNotNull();
    }

    @DisplayName("Test find user by email")
    @MethodSource("generateTestUsers")
    @ParameterizedTest
    void findByEmail(User user) {
        userRepository.save(user);
        User savedUser = userRepository.findByEmail("test@naver.com").get();
        assertThat(user.getId()).isEqualTo(savedUser.getId());
    }

    @DisplayName("Test delete user")
    @MethodSource("generateTestUsers")
    @ParameterizedTest
    void deleteById(User user) {
        userRepository.save(user);
        userRepository.deleteById(user.getId());
        Optional<User> userOptional = userRepository.findById(user.getId());
        assertThatThrownBy(userOptional::get)
            .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("Test update user")
    @MethodSource("generateUpdateTestArguments")
    @ParameterizedTest
    void update(User user, UpdateUserRequest userDto) {
        User savedUser = userRepository.save(user);
        userRepository.update(user.getId(), userDto.toUser());
        User updatedUser = userRepository.findById(savedUser.getId()).get();
        assertThat(updatedUser.getUpdateAt()).isNotNull();
        updatedUser.setUpdateAt(null);
        assertThat(updatedUser.toString()).isNotEqualTo(savedUser.toString());
    }

    @DisplayName("Test find user by nickname")
    @MethodSource("generateTestUsers")
    @ParameterizedTest
    void findByNickName(User user) {
        userRepository.save(user);
        User savedUser = userRepository.findByNickName("test").get();
        assertThat(savedUser.getId()).isEqualTo(user.getId());
    }

    @DisplayName("Test find users by id")
    @MethodSource("generateTestUsers")
    @ParameterizedTest
    void findById(User user) {
        userRepository.save(user);
        User savedUser = userRepository.findById(user.getId()).get();
        assertThat(savedUser.getNickname()).isEqualTo(user.getNickname());
    }

    private static Stream<Arguments> generateTestUsers() {
        return Stream.of(
            Arguments.arguments(User.builder().email("test@naver.com").nickname("test").role("ROLE_USER")
                .phone("01000000000").password("123").status("A").build()
            )
        );
    }

    private static Stream<Arguments> generateUpdateTestArguments() {
        return Stream.of(
            Arguments.arguments(
                User.builder().email("test@naver.com").nickname("test").role("ROLE_USER")
                        .phone("01012345678").password("123").status("A").build(),
                UpdateUserRequest.builder().nickname("tester").role("ROLE_USER")
                    .phone("01000000000").password("1234").status("A").build()
            )
        );
    }
}
