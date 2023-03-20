package com.example.dodam.utils;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.dodam.domain.user.UpdateUserRequest;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class QueryGeneratorTest {

    @DisplayName("When various elements are entered, sql is created dynamically")
    @MethodSource("generateUpdateQueryTestArguments")
    @ParameterizedTest
    void generateUpdateQuery(String sql, UpdateUserRequest userDto) {
        QueryGenerator<UpdateUserRequest> generator = new QueryGenerator<>(userDto);
        String generatedSql = generator.generateDynamicUpdateQuery("user", "where id = :id");
        assertThat(sql).isEqualTo(generatedSql);
    }


    private static Stream<Arguments> generateUpdateQueryTestArguments() {
        return Stream.of(
            Arguments.arguments(
                "UPDATE user SET password=:password, phone=:phone, nickname=:nickname, "
                    + "status=:status, role=:role where id = :id",
                UpdateUserRequest.builder().nickname("test").role("ROLE_USER")
                    .phone("01000000000").password("123").status("A").build()
            )
        );
    }
}
