package com.example.dodam.repository.sms;

import com.example.dodam.domain.sms.Verification;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifyRepository {
    Optional<Verification> findById(String phoneNumber);
    Verification save(Verification verification);
    void delete(Verification verification);
}
