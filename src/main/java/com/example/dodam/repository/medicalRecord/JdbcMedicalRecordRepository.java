package com.example.dodam.repository.medicalRecord;

import com.example.dodam.domain.medicalRecord.MedicalRecord;
import com.example.dodam.domain.medicalRecord.MedicalRecordList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcMedicalRecordRepository implements MedicalRecordRepository {

    String updateQuery = "update medicalrecord set date = ?, detail = ? where id = ?";
    String deleteQuery = "delete from medicalrecord where id = ?";
    String findAllQuery = "select id, date from medicalrecord where userId = ?";
    String findQuery = "select * from medicalrecord where id = ?";

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcMedicalRecordRepository(DataSource source) {
        jdbcTemplate = new JdbcTemplate(source);
        jdbcInsert = new SimpleJdbcInsert(source)       // 데이터소스로 DB에 접근
                .withTableName("medicalRecord")         // "medicalRecord" 테이블에 삽입
                .usingGeneratedKeyColumns("id")             // "id" 컬럼의 값을 key로 반환
                .usingColumns("userId", "date", "detail");  // SimpleJdbcInsert시 사용할 컬럼을 지정
    }

    // 진료기록 등록
    @Override
    public Integer save(MedicalRecord medicalRecord) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(medicalRecord);
        Number key = jdbcInsert.executeAndReturnKey(param);
        medicalRecord.setId(key.intValue());
        return medicalRecord.getId();
    }

    // 진료기록 수정
    @Override
    @Transactional
    public Integer update(MedicalRecord medicalRecord) {
        Integer id = medicalRecord.getId();
        jdbcTemplate.update(updateQuery, medicalRecord.getDate(), medicalRecord.getDetail(), id);
        return id;
    }

    // 진료기록 삭제
    @Override
    public Optional<MedicalRecord> deleteById(Integer id) {
        jdbcTemplate.update(deleteQuery, id);
        return Optional.empty();
    }

    // 진료기록 목록 조회
    @Override
    public List<MedicalRecordList> findAll(Integer userId) {
        return  jdbcTemplate.query(findAllQuery, recordListRowMapper(), userId);
    }

    // 진료기록 조회
    @Override
    public Optional<MedicalRecord> findById(Integer id) {
        List<MedicalRecord> result = jdbcTemplate.query(findQuery, recordRowMapper(), id);
        return result.stream().findAny();
    }



    private RowMapper<MedicalRecord> recordRowMapper(){
        return (rs, rowNum) -> {
            MedicalRecord record = new MedicalRecord();
            record.setId((int) rs.getLong("id"));
            record.setUserId((int) rs.getLong("userId"));
            record.setDate(rs.getDate("date"));
            record.setDetail(rs.getString("detail"));
            return record;
        };
    }
    private RowMapper<MedicalRecordList> recordListRowMapper(){
        return (rs, rowNum) -> {
            MedicalRecordList list = new MedicalRecordList();
            list.setId((int) rs.getLong("id"));
            list.setDate(rs.getDate("date"));
            return list;
        };
    }

}
