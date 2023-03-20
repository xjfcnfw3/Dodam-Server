package com.example.dodam.repository.sms;

import com.example.dodam.domain.sms.Verification;
import com.example.dodam.domain.sms.VerificationStatus;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
public class MysqlVerifyRepository implements VerifyRepository{

    private final NamedParameterJdbcTemplate template;

    public MysqlVerifyRepository(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    @Override
    public Optional<Verification> findById(String phoneNumber) {
        String sql = "select * from verification where phone = :phone";
        try {
            Map<String, Object> param = Map.of("phone", phoneNumber);
            Verification verification = template.queryForObject(sql, param, verificationRowMapper());
            return Optional.ofNullable(verification);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Verification save(Verification verification) {
        String sql = "insert into verification (phone, status, code) values (:phone, :status, :code)";
        log.debug("verification = {}", verification);
        MapSqlParameterSource param = new MapSqlParameterSource()
            .addValue("phone", verification.getId())
            .addValue("status", verification.getStatus().name())
            .addValue("code", verification.getCode());
        try {
            template.update(sql, param);
        } catch (Exception e) {
            delete(verification);
            template.update(sql, param);
        }
        return verification;
    }

    @Override
    public void delete(Verification verification) {
        String sql = "delete from verification where phone = :phone";
        Map<String, Object> param = Map.of("phone", verification.getId());
        template.update(sql, param);
    }

    private RowMapper<Verification> verificationRowMapper() {
        return (rs, rowNum) -> {
            Verification verification = new Verification();
            verification.setStatus(VerificationStatus.valueOf(rs.getString("status")));
            verification.setCode(rs.getString("code"));
            verification.setId(rs.getString("phone"));
            return verification;
        };
    }
}
