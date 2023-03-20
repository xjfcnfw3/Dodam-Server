package com.example.dodam.controller.schedule;

import com.example.dodam.domain.schedule.Schedule;
import com.example.dodam.service.schedule.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ScheduleControllor {

    private final ScheduleService scheduleService;
    @Autowired
    public ScheduleControllor(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    // 일정 등록
    // Schedule 객체 전달, 완료 메세지 반환
    @PostMapping("/schedule")
    public ResponseEntity<?> addSchedule(@RequestBody Schedule schedule){
        scheduleService.save(schedule); //scheduleId 반환
        return new ResponseEntity<>("등록 완료", HttpStatus.CREATED);
    }

    // 일정 수정
    // Schedule 객체 전달, 완료 메세지 반환
    @PutMapping("/schedule")
    public ResponseEntity<?> updateSchedule(@RequestBody Schedule updateSchedule){
        scheduleService.update(updateSchedule); //scheduleId 반환
        return new ResponseEntity<>("수정 완료", HttpStatus.OK);
    }

    // 일정 삭제
    // scheduleId 전달, 완료 메세지 반환
    @DeleteMapping("/schedule/{id}")
    public ResponseEntity<?> deleteSchedule(@PathVariable("id") Integer scheduleId){
        scheduleService.delete(scheduleId);
        return new ResponseEntity<>("삭제 완료", HttpStatus.OK);
    }

    // 일정 목록 조회
    // userId 전달, List<ScheduleList> 반환
    @GetMapping("/schedules/{uid}")
    public ResponseEntity<?> getSchedules(@PathVariable("uid") Integer userId){
        return new ResponseEntity<>(scheduleService.getSchedules(userId), HttpStatus.OK);
    }

    // 일정 조회
    // scheduleId 전달, Schedule 객체 반환
    @GetMapping("/schedule/{id}")
    public ResponseEntity<?> getSchedule(@PathVariable("id") Integer scheduleId){
        return new ResponseEntity<>(scheduleService.getSchedule(scheduleId), HttpStatus.OK);
    }

}
