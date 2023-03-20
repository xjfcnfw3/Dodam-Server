package com.example.dodam.repository.inquiry;

import com.example.dodam.domain.inquiry.Inquiry;
import com.example.dodam.domain.user.User;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
public interface InquiryRepository {
//    Inquiry save(Inquiry inquiry);

    Inquiry save(Inquiry inquiry, MultipartFile file) throws IOException;
    Optional<Inquiry> findById(Long id);
    Optional<Inquiry> deleteById(Long id);
    List<Inquiry> findAll();

    @Transactional
    Inquiry update(Long id, Inquiry inquiry, MultipartFile file) throws IOException;
}
