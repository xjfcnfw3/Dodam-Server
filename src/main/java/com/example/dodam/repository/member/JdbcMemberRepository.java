package com.example.dodam.repository.member;

import com.example.dodam.domain.member.Member;

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
public class JdbcMemberRepository implements MemberRepository {
    private static final String TABLE = "USER";

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcMemberRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
            .withTableName(TABLE)
            .usingGeneratedKeyColumns("id")
            .usingColumns("email", "password", "phone", "nickname", "status", "imgPath", "role",
                "birthdate", "updateAt", "startDate");
    }

    @Override
    @Transactional
    public Member save(Member member) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(member);
        log.debug("user={}", member);
        Number key = jdbcInsert.executeAndReturnKey(param);
        member.setId(key.longValue());
        return member;
    }

    @Override
    public Optional<Member> findByEmail(String email) {
        String sql = "select * from user where email = :email";
        try {
            Map<String, Object> param = Map.of("email", email);
            Member member = template.queryForObject(sql, param, userRowMapper());
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findByUsername(String email) {    // 이거 수정해야함
        String sql = "select * from user where email = :email";
        try {
            Map<String, Object> param = Map.of("email", email);
            Member member = template.queryForObject(sql, param, userRowMapper());
            return Optional.ofNullable(member);
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
    public Member update(Long id, Member member) {
        member.setUpdateAt(LocalDateTime.now());
        QueryGenerator<Member> generator = new QueryGenerator<>(member);
        String sql = generator.generateDynamicUpdateQuery(TABLE, "where id = :id");
        MapSqlParameterSource param = new MapSqlParameterSource();
        param.addValues(generator.generateParams()).addValues(Map.of("id", id));
        log.debug("sql={}, params={}", sql, param);
        template.update(sql, param);
        return member;
    }

    @Override
    public Optional<Member> findByNickName(String nickname) {
        String sql = "select * from user where nickname = :nickname";
        try {
            Map<String, Object> param = Map.of("nickname", nickname);
            Member member = template.queryForObject(sql, param, userRowMapper());
            return Optional.ofNullable(member);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Member> findById(Long id) {
        String sql = "select * from user where id = :id";
        try {
            Map<String, Object> param = Map.of("id", id);
            Member member = template.queryForObject(sql, param, userRowMapper());
            return Optional.ofNullable(member);
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

    private RowMapper<Member> userRowMapper() {
        return BeanPropertyRowMapper.newInstance(Member.class);
    }

}
