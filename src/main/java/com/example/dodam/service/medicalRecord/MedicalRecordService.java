package com.example.dodam.service.medicalRecord;

import com.example.dodam.domain.medicalRecord.MedicalRecord;
import com.example.dodam.domain.medicalRecord.MedicalRecordList;
import com.example.dodam.repository.medicalRecord.MedicalRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
    }

    // 진료기록 등록
    public Integer save(MedicalRecord record){
        return medicalRecordRepository.save(record);
    }

    // 진료기록 수정
    public Integer update(MedicalRecord record){
        return medicalRecordRepository.update(record);
    }

    // 진료기록 삭제
    public void delete(Integer id){
        medicalRecordRepository.deleteById(id);
    }

    // 진료기록 목록 조회
    public List<MedicalRecordList> getRecords(Integer userId){
        return medicalRecordRepository.findAll(userId);
    }

    // 진료기록 조회
    public  MedicalRecord getRecord(Integer id){
        return medicalRecordRepository.findById(id).get();
    }
}
