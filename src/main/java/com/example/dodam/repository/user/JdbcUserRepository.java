package com.example.dodam.repository.user;

import com.example.dodam.domain.user.User;

import com.example.dodam.utils.QueryGenerator;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Repository
public class JdbcUserRepository implements UserRepository {
    private static final String TABLE = "USER";

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcUserRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName(TABLE)
            .usingGeneratedKeyColumns("id")
            .usingColumns("email", "password", "phone", "nickname", "status", "imgPath", "role",
                "birthdate", "updateAt", "startDate");
    }

    @Override
    @Transactional
    public User save(User user) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(user);
        log.debug("user={}", user);
        Number key = jdbcInsert.executeAndReturnKey(param);
        user.setId(key.longValue());
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        String sql = "select * from user where email = :email";
        try {
            Map<String, Object> param = Map.of("email", email);
            User user = template.queryForObject(sql, param, userRowMapper());
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsername(String email) {    // 이거 수정해야함
        String sql = "select * from user where email = :email";
        try {
            Map<String, Object> param = Map.of("email", email);
            User user = template.queryForObject(sql, param, userRowMapper());
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public void deleteById(Long userid) {
        String sql = "delete from user where id = :id";
        Map<String, Object> param = Map.of("id", userid);
        template.update(sql, param);
        log.debug("delete user = {}", userid);
    }

    @Override
    @Transactional
    public User update(Long id, User user) {
        user.setUpdateAt(LocalDateTime.now());
        QueryGenerator<User> generator = new QueryGenerator<>(user);
        String sql = generator.generateDynamicUpdateQuery(TABLE, "where id = :id");
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValues(generator.generateParams()).addValues(Map.of("id", id));
        log.debug("sql={}, params={}", sql, param);
        template.update(sql, param);
        return user;
    }

    @Override
    public Optional<User> findByNickName(String nickname) {
        String sql = "select * from user where nickname = :nickname";
        try {
            Map<String, Object> param = Map.of("nickname", nickname);
            User user = template.queryForObject(sql, param, userRowMapper());
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "select * from user where id = :id";
        try {
            Map<String, Object> param = Map.of("id", id);
            User user = template.queryForObject(sql, param, userRowMapper());
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteImage(Long userId) {
        String sql = "UPDATE USER SET imgPath = null where id = :id";
        Map<String, Object> param = Map.of("id", userId);
        template.update(sql, param);
    }

    @Override
    public void updateStartDate(Long userId, LocalDateTime startDate) {
        String sql = "UPDATE USER SET startDate = :startDate where id = :id";
        Map<String,Object> param = new HashMap<>();
        param.put("id",userId);
        param.put("startDate",startDate);
        template.update(sql, param);
    }

    private RowMapper<User> userRowMapper() {
        return BeanPropertyRowMapper.newInstance(User.class);
    }

}
