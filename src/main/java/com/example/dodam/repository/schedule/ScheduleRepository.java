package com.example.dodam.repository.schedule;

import com.example.dodam.domain.schedule.Schedule;
import com.example.dodam.domain.schedule.ScheduleList;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface ScheduleRepository {
    Integer save(Schedule schedule);    // 일정 등록
    Integer update(Schedule schedule);    // 일정 수정
    Optional<Schedule> deleteById(Integer scheduleId);    // 일정 삭제
    List<ScheduleList> findAll(Integer userId);    // 일정 목록 조회
    Optional<Schedule> findById(Integer scheduleId);    // 일정 조회

}
