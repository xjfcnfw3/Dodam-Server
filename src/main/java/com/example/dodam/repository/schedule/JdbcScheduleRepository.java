package com.example.dodam.repository.schedule;

import com.example.dodam.domain.schedule.Schedule;
import com.example.dodam.domain.schedule.ScheduleList;
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
public class JdbcScheduleRepository implements ScheduleRepository{

    String updateQuery = "update schedule set name = ?, repeatStatus = ?, selectDate = ?, selectDay = ?, startDate = ?, endDate = ?, startTime = ?, endTime = ?, color = ? where scheduleId = ?";
    String deleteQuery = "delete from schedule where scheduleId = ?";
    String findAllQuery = "select scheduleId, name, repeatStatus, selectDate, selectDay, startDate, endDate, color from schedule where userId = ?";
    String findQuery = "select * from schedule where scheduleId = ?";
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    @Autowired
    public JdbcScheduleRepository(DataSource source) {
        jdbcTemplate = new JdbcTemplate(source);
        jdbcInsert = new SimpleJdbcInsert(source)  // 데이터소스로 DB에 접근
                .withTableName("schedule")              // "schedule" 테이블에 삽입
                .usingGeneratedKeyColumns("scheduleId")                 // "scheduleId" 컬럼의 값을 key로 반환
                .usingColumns("userId", "name", "repeatStatus", "selectDate", "selectDay",
                        "startDate", "endDate", "startTime", "endTime", "color");    // SimpleJdbcInsert시 사용할 컬럼을 지정
    }


    // 일정 등록
    @Override
    public Integer save(Schedule schedule) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(schedule);
        Number key = jdbcInsert.executeAndReturnKey(param);
        schedule.setScheduleId(key.intValue());         // scheduleId를 schedule에 저장
        return schedule.getScheduleId();
    }

    // 일정 수정
    @Override
    @Transactional
    public Integer update(Schedule schedule) {
        Integer id = schedule.getScheduleId();
        jdbcTemplate.update(updateQuery, schedule.getName(), schedule.getRepeatStatus(), schedule.getSelectDate(), schedule.getSelectDay(),
                schedule.getStartDate(), schedule.getEndDate(), schedule.getStartTime(), schedule.getEndTime(), schedule.getColor(), id );
        return id;
    }

    // 일정 삭제
    @Override
    public Optional<Schedule> deleteById(Integer scheduleId) {
        jdbcTemplate.update(deleteQuery, scheduleId);
        return Optional.empty();
    }

    // 일정 목록 조회
    @Override
    public List<ScheduleList> findAll(Integer userId) {
        return jdbcTemplate.query(findAllQuery, scheduleListRowMapper(), userId);
    }

    // 일정 조회
    @Override
    public Optional<Schedule> findById(Integer scheduleId) {
        List<Schedule> result = jdbcTemplate.query(findQuery, scheduleRowMapper(), scheduleId);
        return result.stream().findAny();
    }






    private RowMapper<Schedule> scheduleRowMapper(){
        return (rs, rowNum) -> {
            Schedule schedule = new Schedule();
            schedule.setScheduleId((int) rs.getLong("scheduleId"));
            schedule.setUserId((int) rs.getLong("userId"));
            schedule.setName(rs.getString("name"));
            schedule.setRepeatStatus(rs.getString("repeatStatus"));
            schedule.setSelectDate(rs.getString("selectDate"));
            schedule.setSelectDay((int) rs.getLong("selectDay"));
            schedule.setStartDate(rs.getDate("startDate"));
            schedule.setEndDate(rs.getDate("endDate"));
            schedule.setStartTime(rs.getTime("startTime"));
            schedule.setEndTime(rs.getTime("endTime"));
            schedule.setColor(rs.getString("color"));
            return schedule;
        };
    }
    private RowMapper<ScheduleList> scheduleListRowMapper(){
        return (rs, rowNum) -> {
            ScheduleList list = new ScheduleList();
            list.setScheduleId((int) rs.getLong("scheduleId"));
            list.setName(rs.getString("name"));
            list.setRepeatStatus(rs.getString("repeatStatus"));
            list.setSelectDate(rs.getString("selectDate"));
            list.setSelectDay((int) rs.getLong("selectDay"));
            list.setStartDate(rs.getDate("startDate"));
            list.setEndDate(rs.getDate("endDate"));
            list.setColor(rs.getString("color"));
            return list;
        };
    }

}
