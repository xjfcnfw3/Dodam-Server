package com.example.dodam.repository.medicalRecord;

import com.example.dodam.domain.medicalRecord.MedicalRecord;
import com.example.dodam.domain.medicalRecord.MedicalRecordList;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalRecordRepository {
    Integer save(MedicalRecord record);     // 진료기록 등록
    Integer update(MedicalRecord record);    // 진료기록 수정
    Optional<MedicalRecord> deleteById(Integer id);    // 진료기록 삭제
    List<MedicalRecordList> findAll(Integer userId);   // 진료기록 목록 조회
    Optional<MedicalRecord> findById(Integer id);      // 진료기록 조회
}
