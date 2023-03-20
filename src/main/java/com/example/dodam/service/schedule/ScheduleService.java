package com.example.dodam.service.schedule;

import com.example.dodam.domain.schedule.Schedule;
import com.example.dodam.domain.schedule.ScheduleList;
import com.example.dodam.repository.schedule.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    @Autowired
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }


    // 일정 등록
    public Integer save(Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    // 일정 수정
    public Integer update(Schedule schedule){
        return scheduleRepository.update(schedule);
    }

    // 일정 삭제
    public void delete(Integer id){
        scheduleRepository.deleteById(id);
    }

    // 일정 목록 조회
    public List<ScheduleList> getSchedules(Integer userId){
        return scheduleRepository.findAll(userId);
    }

    // 일정 조회
    public Schedule getSchedule(Integer id){
        return scheduleRepository.findById(id).get();
    }
}
